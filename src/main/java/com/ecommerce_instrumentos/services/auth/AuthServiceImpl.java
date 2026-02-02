package com.ecommerce_instrumentos.services.auth;

import com.ecommerce_instrumentos.dto.SignupRequest;
import com.ecommerce_instrumentos.dto.UserDto;
import com.ecommerce_instrumentos.entity.User;
import com.ecommerce_instrumentos.enums.UserRole;
import com.ecommerce_instrumentos.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto createUser(SignupRequest signupRequest) {
        User user = new User();

        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setLastName(signupRequest.getLastName());  // Agregamos el campo lastName
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);

        UserDto userDto = convertToUserDto(createdUser);

        return userDto;
    }

    public Boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    private UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setLastName(user.getLastName());  // Agregamos el campo lastName
        userDto.setUserRole(user.getRole());

        // Puedes agregar otros campos según sea necesario

        return userDto;
    }

    public List<UserDto> listAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void createAdminAccount() {
        List<User> adminAccounts = userRepository.findByRole(UserRole.ADMIN);

        if (adminAccounts.isEmpty()) {
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setLastName("adminLastName");  // Agregamos el campo lastName
            user.setRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);
        }
    }






    @Override
    public UserDto getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return convertToUserDto(user);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }
    }





}
