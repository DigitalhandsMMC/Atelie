package com.example.clothesshop.request;

import lombok.Data;

@Data
public class CustomerDetailRequest {

    private Long id;

    private double weight;

    private double height;

    private double sideMeasurement;

    private double shoulderMeasurement;

    private double waistMeasurement;

    private Long userId;

}