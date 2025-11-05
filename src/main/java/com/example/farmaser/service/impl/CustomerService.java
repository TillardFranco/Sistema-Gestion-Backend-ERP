package com.example.farmaser.service.impl;

import com.example.farmaser.config.AuditHelper;
import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.customerMapper.CustomerListMapper;
import com.example.farmaser.mapper.customerMapper.CustomerRequestMapper;
import com.example.farmaser.mapper.customerMapper.CustomerResponseMapper;
import com.example.farmaser.model.dto.customerDto.CustomerRequestDto;
import com.example.farmaser.model.dto.customerDto.CustomerResponseDto;
import com.example.farmaser.model.entity.ActionType;
import com.example.farmaser.model.entity.CustomerEntity;
import com.example.farmaser.model.repository.CustomerRepository;
import com.example.farmaser.service.ICustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService implements ICustomer {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerResponseMapper customerResponseMapper;

    @Autowired
    private CustomerRequestMapper customerRequestMapper;

    @Autowired
    private CustomerListMapper customerListMapper;

    @Autowired
    private AuditHelper auditHelper;

    @Transactional(readOnly = true)
    @Override
    public Page<CustomerResponseDto> listAll(Pageable pageable) {
        Page<CustomerEntity> page = customerRepository.findByActiveTrue(pageable);
        List<CustomerResponseDto> dtos = page.getContent().stream()
                .map(customerResponseMapper::customerEntityToCustomerResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerResponseDto> listAll() {
        List<CustomerEntity> entities = customerRepository.findByActiveTrue();
        return customerListMapper.customerEntityToCustomerList(entities);
    }

    @Transactional
    @Override
    public CustomerResponseDto save(CustomerRequestDto requestDto) {
        if (customerRepository.existsByDni(requestDto.getDni())) {
            throw new BadRequestException("Ya existe un cliente con el DNI: " + requestDto.getDni());
        }

        if (requestDto.getEmail() != null && !requestDto.getEmail().isBlank()) {
            if (customerRepository.existsByEmail(requestDto.getEmail())) {
                throw new BadRequestException("Ya existe un cliente con el email: " + requestDto.getEmail());
            }
        }

        CustomerEntity entity = customerRequestMapper.customerRequestDtoToCustomerEntity(requestDto);
        if (entity.getActive() == null) {
            entity.setActive(true);
        }
        CustomerEntity saved = customerRepository.save(entity);
        
        // Registrar auditoría
        auditHelper.log("Customer", saved.getId(), ActionType.CREATE, null,
                auditHelper.toJsonString(saved), "Cliente creado: " + saved.getName() + " " + saved.getLastname() + " (DNI: " + saved.getDni() + ")");
        
        return customerResponseMapper.customerEntityToCustomerResponseDto(saved);
    }

    @Transactional
    @Override
    public CustomerResponseDto update(Long id, CustomerRequestDto requestDto) {
        CustomerEntity existing = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con id " + id + " no encontrado"));

        if (requestDto.getDni() != null && !requestDto.getDni().equals(existing.getDni())
                && customerRepository.existsByDni(requestDto.getDni())) {
            throw new BadRequestException("Ya existe otro cliente con el DNI: " + requestDto.getDni());
        }

        if (requestDto.getEmail() != null && !requestDto.getEmail().isBlank()
                && !requestDto.getEmail().equals(existing.getEmail())
                && customerRepository.existsByEmailAndIdNot(requestDto.getEmail(), id)) {
            throw new BadRequestException("Ya existe otro cliente con el email: " + requestDto.getEmail());
        }

        // Guardar valores antiguos para auditoría
        String oldValue = auditHelper.toJsonString(existing);

        CustomerEntity updated = customerRequestMapper.customerRequestDtoToCustomerEntity(requestDto);
        updated.setId(existing.getId());
        updated.setCreationDate(existing.getCreationDate());
        if (updated.getActive() == null) {
            updated.setActive(existing.getActive());
        }

        CustomerEntity saved = customerRepository.save(updated);
        
        // Registrar auditoría
        auditHelper.log("Customer", saved.getId(), ActionType.UPDATE, oldValue,
                auditHelper.toJsonString(saved), "Cliente actualizado: " + saved.getName() + " " + saved.getLastname() + " (DNI: " + saved.getDni() + ")");
        
        return customerResponseMapper.customerEntityToCustomerResponseDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponseDto findByDni(String dni) {
        CustomerEntity entity = customerRepository.findByDni(dni)
                .orElseThrow(() -> new NotFoundException("Cliente con DNI " + dni + " no encontrado"));
        return customerResponseMapper.customerEntityToCustomerResponseDto(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CustomerResponseDto> searchByName(String term, Pageable pageable) {
        Page<CustomerEntity> page = customerRepository
                .findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCaseAndActiveTrue(term, term, pageable);
        List<CustomerResponseDto> dtos = page.getContent().stream()
                .map(customerResponseMapper::customerEntityToCustomerResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerResponseDto> searchByName(String term) {
        List<CustomerEntity> entities = customerRepository
                .findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCaseAndActiveTrue(term, term);
        return customerListMapper.customerEntityToCustomerList(entities);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CustomerResponseDto> searchByEmail(String email, Pageable pageable) {
        Page<CustomerEntity> page = customerRepository.findByEmailContainingIgnoreCaseAndActiveTrue(email, pageable);
        List<CustomerResponseDto> dtos = page.getContent().stream()
                .map(customerResponseMapper::customerEntityToCustomerResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerResponseDto> searchByEmail(String email) {
        List<CustomerEntity> entities = customerRepository.findByEmailContainingIgnoreCaseAndActiveTrue(email);
        return customerListMapper.customerEntityToCustomerList(entities);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con id " + id + " no encontrado"));
        
        // Guardar valores antiguos para auditoría
        String oldValue = auditHelper.toJsonString(entity);
        
        entity.setActive(false);
        CustomerEntity deleted = customerRepository.save(entity);
        
        // Registrar auditoría
        auditHelper.log("Customer", deleted.getId(), ActionType.DELETE, oldValue,
                auditHelper.toJsonString(deleted), "Cliente eliminado (soft delete): " + deleted.getName() + " " + deleted.getLastname() + " (DNI: " + deleted.getDni() + ")");
    }
}


