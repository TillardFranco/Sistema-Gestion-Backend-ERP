package com.example.farmaser.controller;

import com.example.farmaser.model.dto.customerDto.CustomerRequestDto;
import com.example.farmaser.model.dto.customerDto.CustomerResponseDto;
import com.example.farmaser.service.ICustomer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private ICustomer customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<CustomerResponseDto> customers = customerService.listAll(pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerResponseDto>> listAllWithoutPagination() {
        List<CustomerResponseDto> customers = customerService.listAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<CustomerResponseDto> findByDni(@PathVariable String dni) {
        CustomerResponseDto customer = customerService.findByDni(dni);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CustomerResponseDto>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<CustomerResponseDto> customers = customerService.searchByName(name, pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search/all")
    public ResponseEntity<List<CustomerResponseDto>> searchByNameWithoutPagination(@RequestParam String name) {
        List<CustomerResponseDto> customers = customerService.searchByName(name);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search/email")
    public ResponseEntity<Page<CustomerResponseDto>> searchByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "email") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<CustomerResponseDto> customers = customerService.searchByEmail(email, pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search/email/all")
    public ResponseEntity<List<CustomerResponseDto>> searchByEmailWithoutPagination(@RequestParam String email) {
        List<CustomerResponseDto> customers = customerService.searchByEmail(email);
        return ResponseEntity.ok(customers);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> create(@Valid @RequestBody CustomerRequestDto requestDto) {
        CustomerResponseDto created = customerService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDto requestDto) {
        CustomerResponseDto updated = customerService.update(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


