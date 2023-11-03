package com.example.clothesshop.reponse;

import lombok.Data;

@Data
public class CustomerDetailResponse {

    private Long id;

    private double weight;

    private double height;

    private double sideMeasurement;

    private double shoulderMeasurement;

    private double waistMeasurement;

    private UserResponse userResponse;

}