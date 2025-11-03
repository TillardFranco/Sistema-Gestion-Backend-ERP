package com.example.farmaser.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "customer")
public class CustomerEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dni", nullable = false, unique = true, length = 20)
    private String dni;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastname;

    @Column(name = "email", length = 150, unique = true)
    private String email;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "creation_date", updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
}


