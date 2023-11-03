package com.example.clothesshop.reponse;

import com.example.clothesshop.enums.UserRole;
import lombok.Data;

@Data
public class UserResponse {

    private Long id;


    private String name;
    private String surname;
    private String email;
    private String password;
    private UserRole userRole;

//    private Long customerId;
}