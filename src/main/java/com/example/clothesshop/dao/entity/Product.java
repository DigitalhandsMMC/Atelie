package com.example.clothesshop.dao.entity;


import com.example.clothesshop.enums.Length;
import com.example.clothesshop.enums.PropertyType;
import com.example.clothesshop.enums.Size;
import com.example.clothesshop.enums.Style;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private Double price;

    private String color;

//    @Column(name = "size")
//    @Enumerated(EnumType.STRING)
    private String size;

    @Column(name = "style")
    @Enumerated(EnumType.STRING)
    private Style style;

    @Column(name = "length")
    @Enumerated(EnumType.STRING)
    private Length length;

    @Column(name = "property_type")
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;


}

