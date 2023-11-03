package com.example.clothesshop.dao.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "customer")
@Entity
public class CustomerDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "weight")
    private double weight;

    @Column(name = "height")
    private double height;

    @Column(name = "side_measurement")
    private double sideMeasurement;

    @Column(name = "shoulder_measurement")
    private double shoulderMeasurement;

    @Column(name = "waist_measurement")
    private double waistMeasurement;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;

    @Column(name = "user_Id")
    private Long userId;

}
