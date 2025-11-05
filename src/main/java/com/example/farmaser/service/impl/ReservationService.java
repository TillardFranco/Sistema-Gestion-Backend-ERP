package com.example.farmaser.service.impl;

import com.example.farmaser.config.AuditHelper;
import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.reservationMapper.ReservationRequestMapper;
import com.example.farmaser.mapper.reservationMapper.ReservationResponseMapper;
import com.example.farmaser.model.dto.reservationDto.ReservationRequestDto;
import com.example.farmaser.model.dto.reservationDto.ReservationResponseDto;
import com.example.farmaser.model.dto.saleDto.SaleRequestDto;
import com.example.farmaser.model.entity.ActionType;
import com.example.farmaser.model.entity.*;
import com.example.farmaser.model.repository.*;
import com.example.farmaser.service.IReservation;
import com.example.farmaser.service.ISale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationService implements IReservation {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReservationRequestMapper reservationRequestMapper;

    @Autowired
    private ReservationResponseMapper reservationResponseMapper;

    @Autowired
    private ISale saleService;

    @Autowired
    private AuditHelper auditHelper;

    private static final int RESERVATION_EXPIRATION_DAYS = 7; // Días para expirar una reserva

    @Transactional
    @Override
    public ReservationResponseDto create(ReservationRequestDto requestDto) {
        // Validar que el cliente exista
        CustomerEntity customer = customerRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        // Validar que el producto exista
        ProductEntity product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        // Validar cantidad
        if (requestDto.getQuantity() == null || requestDto.getQuantity() <= 0) {
            throw new BadRequestException("La cantidad debe ser mayor a cero");
        }

        // Validar stock disponible
        if (product.getStock() < requestDto.getQuantity()) {
            throw new BadRequestException("Stock insuficiente. Disponible: " + product.getStock() + 
                ", solicitado: " + requestDto.getQuantity());
        }

        // Reservar stock: restar del stock disponible
        product.setStock(product.getStock() - requestDto.getQuantity());
        productRepository.save(product);

        // Calcular fecha de expiración (7 días desde ahora)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, RESERVATION_EXPIRATION_DAYS);
        Date expirationDate = calendar.getTime();

        // Crear la reserva
        ReservationEntity reservation = ReservationEntity.builder()
                .reservationNumber(generateReservationNumber())
                .customer(customer)
                .product(product)
                .quantity(requestDto.getQuantity())
                .status(ReservationStatus.PENDING)
                .expirationDate(expirationDate)
                .notes(requestDto.getNotes())
                .build();

        ReservationEntity saved = reservationRepository.save(reservation);
        
        // Registrar auditoría
        auditHelper.log("Reservation", saved.getId(), ActionType.CREATE, null,
                auditHelper.toJsonString(saved), "Reserva creada: " + saved.getReservationNumber() + " - Producto: " + saved.getProduct().getName() + " - Cantidad: " + saved.getQuantity());
        
        return reservationResponseMapper.reservationEntityToReservationResponseDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ReservationResponseDto> listAll(Pageable pageable) {
        Page<ReservationEntity> page = reservationRepository.findAll(pageable);
        List<ReservationResponseDto> dtos = page.getContent().stream()
                .map(reservationResponseMapper::reservationEntityToReservationResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ReservationResponseDto> listByStatus(ReservationStatus status, Pageable pageable) {
        Page<ReservationEntity> page = reservationRepository.findByStatus(status, pageable);
        List<ReservationResponseDto> dtos = page.getContent().stream()
                .map(reservationResponseMapper::reservationEntityToReservationResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public ReservationResponseDto findById(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva con id " + id + " no encontrada"));
        return reservationResponseMapper.reservationEntityToReservationResponseDto(reservation);
    }

    @Transactional(readOnly = true)
    @Override
    public ReservationResponseDto findByReservationNumber(String reservationNumber) {
        ReservationEntity reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new NotFoundException("Reserva con número " + reservationNumber + " no encontrada"));
        return reservationResponseMapper.reservationEntityToReservationResponseDto(reservation);
    }

    @Transactional
    @Override
    public ReservationResponseDto confirm(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva no encontrada"));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new BadRequestException("Solo se pueden confirmar reservas pendientes");
        }

        if (new Date().after(reservation.getExpirationDate())) {
            throw new BadRequestException("La reserva ha expirado");
        }

        // Guardar valores antiguos para auditoría
        String oldValue = auditHelper.toJsonString(reservation);
        
        reservation.setStatus(ReservationStatus.CONFIRMED);
        ReservationEntity saved = reservationRepository.save(reservation);
        
        // Registrar auditoría
        auditHelper.log("Reservation", saved.getId(), ActionType.CONFIRM, oldValue,
                auditHelper.toJsonString(saved), "Reserva confirmada: " + saved.getReservationNumber());
        
        return reservationResponseMapper.reservationEntityToReservationResponseDto(saved);
    }

    @Transactional
    @Override
    public ReservationResponseDto complete(Long id, SaleRequestDto saleRequestDto, String userEmail) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva no encontrada"));

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new BadRequestException("La reserva ya está completada");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED || reservation.getStatus() == ReservationStatus.EXPIRED) {
            throw new BadRequestException("No se puede completar una reserva cancelada o expirada");
        }

        // El stock ya fue descontado cuando se creó la reserva.
        // Para que SaleService pueda crear la venta correctamente, necesitamos
        // temporalmente restaurar el stock del producto reservado antes de crear la venta,
        // así SaleService lo descontará nuevamente y quedará correcto.
        ProductEntity reservedProduct = reservation.getProduct();
        int reservedQuantity = reservation.getQuantity();
        
        // Restaurar temporalmente el stock para que SaleService pueda validar y descontar
        reservedProduct.setStock(reservedProduct.getStock() + reservedQuantity);
        productRepository.save(reservedProduct);

        // Crear la venta usando el servicio de ventas
        // SaleService validará el stock y lo descontará nuevamente
        SaleRequestDto saleDto = SaleRequestDto.builder()
                .customerId(reservation.getCustomer().getId())
                .paymentMethod(saleRequestDto.getPaymentMethod())
                .items(saleRequestDto.getItems())
                .build();

        // Crear la venta (esto descontará el stock nuevamente, quedando correcto)
        saleService.create(saleDto, userEmail);

        // Guardar valores antiguos para auditoría
        String oldValue = auditHelper.toJsonString(reservation);
        
        // Marcar reserva como completada
        reservation.setStatus(ReservationStatus.COMPLETED);
        ReservationEntity saved = reservationRepository.save(reservation);

        // Registrar auditoría
        auditHelper.log("Reservation", saved.getId(), ActionType.COMPLETE, oldValue,
                auditHelper.toJsonString(saved), "Reserva completada: " + saved.getReservationNumber() + " - Convertida en venta");

        return reservationResponseMapper.reservationEntityToReservationResponseDto(saved);
    }

    @Transactional
    @Override
    public ReservationResponseDto cancel(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva no encontrada"));

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new BadRequestException("No se puede cancelar una reserva completada");
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BadRequestException("La reserva ya está cancelada");
        }

        // Liberar stock: devolver el stock reservado al producto
        ProductEntity product = reservation.getProduct();
        product.setStock(product.getStock() + reservation.getQuantity());
        productRepository.save(product);

        // Guardar valores antiguos para auditoría
        String oldValue = auditHelper.toJsonString(reservation);
        
        // Marcar como cancelada
        reservation.setStatus(ReservationStatus.CANCELLED);
        ReservationEntity saved = reservationRepository.save(reservation);

        // Registrar auditoría
        auditHelper.log("Reservation", saved.getId(), ActionType.CANCEL, oldValue,
                auditHelper.toJsonString(saved), "Reserva cancelada: " + saved.getReservationNumber() + " - Stock liberado");

        return reservationResponseMapper.reservationEntityToReservationResponseDto(saved);
    }

    @Transactional
    @Override
    public void expireReservations() {
        Date now = new Date();
        List<ReservationEntity> expiredReservations = reservationRepository
                .findByExpirationDateBeforeAndStatus(now, ReservationStatus.PENDING);

        expiredReservations.addAll(reservationRepository
                .findByExpirationDateBeforeAndStatus(now, ReservationStatus.CONFIRMED));

        for (ReservationEntity reservation : expiredReservations) {
            // Guardar valores antiguos para auditoría
            String oldValue = auditHelper.toJsonString(reservation);
            
            // Liberar stock de las reservas expiradas
            ProductEntity product = reservation.getProduct();
            product.setStock(product.getStock() + reservation.getQuantity());
            productRepository.save(product);

            // Marcar como expirada
            reservation.setStatus(ReservationStatus.EXPIRED);
            ReservationEntity saved = reservationRepository.save(reservation);
            
            // Registrar auditoría
            auditHelper.log("Reservation", saved.getId(), ActionType.EXPIRE, oldValue,
                    auditHelper.toJsonString(saved), "Reserva expirada automáticamente: " + saved.getReservationNumber() + " - Stock liberado");
        }
    }

    private String generateReservationNumber() {
        return "R-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

