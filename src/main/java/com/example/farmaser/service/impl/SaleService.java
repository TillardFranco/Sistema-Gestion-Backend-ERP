package com.example.farmaser.service.impl;

import com.example.farmaser.config.AuditHelper;
import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.saleMapper.SaleItemRequestMapper;
import com.example.farmaser.mapper.saleMapper.SaleResponseMapper;
import com.example.farmaser.model.dto.saleDto.SaleItemRequestDto;
import com.example.farmaser.model.dto.saleDto.SaleRequestDto;
import com.example.farmaser.model.dto.saleDto.SaleResponseDto;
import com.example.farmaser.model.entity.ActionType;
import com.example.farmaser.model.entity.*;
import com.example.farmaser.model.repository.*;
import com.example.farmaser.service.ISale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SaleService implements ISale {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SaleItemRequestMapper saleItemRequestMapper;

    @Autowired
    private SaleResponseMapper saleResponseMapper;

    @Autowired
    private AuditHelper auditHelper;

    @Transactional
    @Override
    public SaleResponseDto create(SaleRequestDto requestDto, String userEmail) {
        if (requestDto.getItems() == null || requestDto.getItems().isEmpty()) {
            throw new BadRequestException("La venta debe contener al menos un item");
        }

        CustomerEntity customer = customerRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Validar stock y calcular totales
        BigDecimal subtotal = BigDecimal.ZERO;
        for (SaleItemRequestDto item : requestDto.getItems()) {
            ProductEntity product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + item.getProductId()));

            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new BadRequestException("Cantidad inválida para producto: " + product.getName());
            }

            if (product.getStock() < item.getQuantity()) {
                throw new BadRequestException("Stock insuficiente para producto: " + product.getName() + 
                    ". Stock disponible: " + product.getStock() + ", solicitado: " + item.getQuantity());
            }

            BigDecimal lineSubtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(lineSubtotal);
        }

        // Calcular impuestos (IVA 21% - ajustable)
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.21"));
        BigDecimal total = subtotal.add(tax);

        // Crear la venta
        SaleEntity sale = SaleEntity.builder()
                .saleNumber(generateSaleNumber())
                .customer(customer)
                .user(user)
                .subtotal(subtotal)
                .tax(tax)
                .total(total)
                .paymentMethod(requestDto.getPaymentMethod())
                .status(SaleStatus.COMPLETED)
                .build();

        SaleEntity savedSale = saleRepository.save(sale);

        // Crear items y actualizar stock
        for (SaleItemRequestDto itemDto : requestDto.getItems()) {
            ProductEntity product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + itemDto.getProductId()));

            // Actualizar stock del producto
            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product);

            // Calcular subtotal del item
            BigDecimal itemSubtotal = itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));

            // Crear el item de la venta
            SaleItemEntity saleItem = SaleItemEntity.builder()
                    .sale(savedSale)
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(itemDto.getUnitPrice())
                    .subtotal(itemSubtotal)
                    .build();

            saleItemRepository.save(saleItem);
        }

        // Refrescar la venta con los items cargados
        SaleEntity finalSale = saleRepository.findById(savedSale.getId())
                .orElseThrow(() -> new NotFoundException("Venta no encontrada tras guardar"));

        // Registrar auditoría
        auditHelper.log("Sale", finalSale.getId(), ActionType.CREATE, null,
                auditHelper.toJsonString(finalSale), "Venta creada: " + finalSale.getSaleNumber() + " - Total: $" + finalSale.getTotal());

        return saleResponseMapper.saleEntityToSaleResponseDto(finalSale);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SaleResponseDto> listAll(Pageable pageable) {
        Page<SaleEntity> page = saleRepository.findAll(pageable);
        List<SaleResponseDto> dtos = page.getContent().stream()
                .map(saleResponseMapper::saleEntityToSaleResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SaleResponseDto> listByStatus(SaleStatus status, Pageable pageable) {
        Page<SaleEntity> page = saleRepository.findByStatus(status, pageable);
        List<SaleResponseDto> dtos = page.getContent().stream()
                .map(saleResponseMapper::saleEntityToSaleResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SaleResponseDto> listByDateRange(Date start, Date end, Pageable pageable) {
        Page<SaleEntity> page = saleRepository.findByDateBetween(start, end, pageable);
        List<SaleResponseDto> dtos = page.getContent().stream()
                .map(saleResponseMapper::saleEntityToSaleResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public SaleResponseDto findBySaleNumber(String saleNumber) {
        SaleEntity sale = saleRepository.findBySaleNumber(saleNumber)
                .orElseThrow(() -> new NotFoundException("Venta con número " + saleNumber + " no encontrada"));
        return saleResponseMapper.saleEntityToSaleResponseDto(sale);
    }

    @Transactional
    @Override
    public SaleResponseDto cancel(Long id) {
        SaleEntity sale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venta con id " + id + " no encontrada"));

        if (sale.getStatus() == SaleStatus.CANCELLED) {
            throw new BadRequestException("La venta ya está cancelada");
        }

        // Guardar valores antiguos para auditoría
        String oldValue = auditHelper.toJsonString(sale);

        // Revertir stock de todos los items
        List<SaleItemEntity> items = saleItemRepository.findBySale(sale);
        for (SaleItemEntity item : items) {
            ProductEntity product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        // Marcar venta como cancelada
        sale.setStatus(SaleStatus.CANCELLED);
        SaleEntity saved = saleRepository.save(sale);

        // Registrar auditoría
        auditHelper.log("Sale", saved.getId(), ActionType.CANCEL, oldValue,
                auditHelper.toJsonString(saved), "Venta cancelada: " + saved.getSaleNumber());

        return saleResponseMapper.saleEntityToSaleResponseDto(saved);
    }

    private String generateSaleNumber() {
        return "S-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

