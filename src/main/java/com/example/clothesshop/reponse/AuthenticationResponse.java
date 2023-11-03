package com.example.clothesshop.reponse;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String JwtToken;
    private Long userId;
}
