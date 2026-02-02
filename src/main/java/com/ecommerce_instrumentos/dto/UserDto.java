package com.ecommerce_instrumentos.dto;

import com.ecommerce_instrumentos.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String email;
    private String name;
    private String lastName;
    private UserRole userRole;

}
