package com.ecommerce_instrumentos.services.auth;

import com.ecommerce_instrumentos.dto.SignupRequest;
import com.ecommerce_instrumentos.dto.UserDto;
import com.ecommerce_instrumentos.enums.UserRole;

import java.util.List;

public interface AuthService {

    UserDto createUser(SignupRequest signupRequest);

    Boolean hasUserWithEmail(String email);

    List<UserDto> listAllUsers();


    UserDto getUserById(Long userId);



}
