package com.example.clothesshop.dao.entity;

import com.example.clothesshop.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String email;
    private String password;
    @Column(name = "userRole")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

//    @OneToOne(mappedBy = "user")
//    private Customer customer;
}
