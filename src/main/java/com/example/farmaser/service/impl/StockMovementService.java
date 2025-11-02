package com.example.farmaser.service.impl;

import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.stockMapper.StockMovementMapper;
import com.example.farmaser.model.dto.stockDto.StockMovementRequestDto;
import com.example.farmaser.model.dto.stockDto.StockMovementResponseDto;
import com.example.farmaser.model.entity.MovementType;
import com.example.farmaser.model.entity.ProductEntity;
import com.example.farmaser.model.entity.StockMovementEntity;
import com.example.farmaser.model.entity.UserEntity;
import com.example.farmaser.model.repository.ProductRepository;
import com.example.farmaser.model.repository.StockMovementRepository;
import com.example.farmaser.model.repository.UserRepository;
import com.example.farmaser.service.IStockMovement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockMovementService implements IStockMovement {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockMovementMapper stockMovementMapper;

    @Transactional
    @Override
    public StockMovementResponseDto registerEntry(StockMovementRequestDto stockMovementRequestDto, String userEmail) {
        if (stockMovementRequestDto.getType() != MovementType.IN &&
                stockMovementRequestDto.getType() != MovementType.ADJUSTMENT &&
                stockMovementRequestDto.getType() != MovementType.RETURN) {
            throw new BadRequestException("Este método solo permite movimientos de tipo IN, ADJUSTMENT o RETURN");
        }

        return registerMovement(stockMovementRequestDto, userEmail);
    }

    @Transactional
    @Override
    public StockMovementResponseDto registerExit(StockMovementRequestDto stockMovementRequestDto, String userEmail) {
        if (stockMovementRequestDto.getType() != MovementType.OUT) {
            throw new BadRequestException("Este método solo permite movimientos de tipo OUT");
        }

        return registerMovement(stockMovementRequestDto, userEmail);
    }

    private StockMovementResponseDto registerMovement(StockMovementRequestDto requestDto, String userEmail) {
        // Obtener producto
        ProductEntity product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Producto con ID " + requestDto.getProductId() + " no encontrado"));

        // Obtener usuario
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Usuario con email " + userEmail + " no encontrado"));

        // Validar stock disponible para egresos
        if (requestDto.getType() == MovementType.OUT) {
            if (product.getStock() < requestDto.getQuantity()) {
                throw new BadRequestException(
                        "Stock insuficiente. Stock disponible: " + product.getStock() +
                                ", cantidad solicitada: " + requestDto.getQuantity());
            }
            // Reducir stock
            product.setStock(product.getStock() - requestDto.getQuantity());
        } else if (requestDto.getType() == MovementType.IN || requestDto.getType() == MovementType.RETURN) {
            // Aumentar stock
            product.setStock(product.getStock() + requestDto.getQuantity());
        } else if (requestDto.getType() == MovementType.ADJUSTMENT) {
            // Ajuste: establecer stock directamente
            if (requestDto.getQuantity() < 0) {
                throw new BadRequestException("La cantidad en un ajuste no puede ser negativa");
            }
            product.setStock(requestDto.getQuantity());
        }

        // Guardar producto actualizado
        productRepository.save(product);

        // Crear movimiento de stock
        StockMovementEntity movement = StockMovementEntity.builder()
                .product(product)
                .type(requestDto.getType())
                .quantity(requestDto.getQuantity())
                .reason(requestDto.getReason())
                .user(user)
                .notes(requestDto.getNotes())
                .build();

        StockMovementEntity savedMovement = stockMovementRepository.save(movement);
        return stockMovementMapper.stockMovementEntityToStockMovementResponseDto(savedMovement);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StockMovementResponseDto> getMovementsByProduct(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Producto con ID " + productId + " no encontrado"));

        List<StockMovementEntity> movements = stockMovementRepository.findByProductOrderByDateDesc(product);
        return stockMovementMapper.stockMovementEntityListToStockMovementResponseDtoList(movements);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StockMovementResponseDto> getAllMovements() {
        List<StockMovementEntity> movements = stockMovementRepository.findAllByOrderByDateDesc();
        return stockMovementMapper.stockMovementEntityListToStockMovementResponseDtoList(movements);
    }
}


