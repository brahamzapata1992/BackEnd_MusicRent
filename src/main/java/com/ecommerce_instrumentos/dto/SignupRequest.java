package com.ecommerce_instrumentos.dto;

import lombok.Data;

@Data
public class SignupRequest {

    private String email;
    private String password;
    private String name;
    private String lastName;

}
