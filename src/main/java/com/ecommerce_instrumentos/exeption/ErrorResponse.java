package com.ecommerce_instrumentos.exeption;

import lombok.Data;

@Data
public class ErrorResponse {

    private int status;
    private String message;

}
