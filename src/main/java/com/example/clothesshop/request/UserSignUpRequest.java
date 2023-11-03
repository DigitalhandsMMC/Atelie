package com.example.clothesshop.request;

import com.example.clothesshop.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import com.example.clothesshop.enums.UserRole;

@Data
public class UserSignUpRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;



}
