package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity, Long>, CrudRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByDni(String dni);

    boolean existsByDni(String dni);

    Optional<CustomerEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Page<CustomerEntity> findByActiveTrue(Pageable pageable);

    List<CustomerEntity> findByActiveTrue();

    Page<CustomerEntity> findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCaseAndActiveTrue(String name, String lastname, Pageable pageable);

    List<CustomerEntity> findByNameContainingIgnoreCaseOrLastnameContainingIgnoreCaseAndActiveTrue(String name, String lastname);

    Page<CustomerEntity> findByEmailContainingIgnoreCaseAndActiveTrue(String email, Pageable pageable);

    List<CustomerEntity> findByEmailContainingIgnoreCaseAndActiveTrue(String email);
}


