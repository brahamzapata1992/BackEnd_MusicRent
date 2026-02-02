package com.ecommerce_instrumentos.controller;
import com.ecommerce_instrumentos.dto.AuthenticationRequest;
import com.ecommerce_instrumentos.dto.SignupRequest;
import com.ecommerce_instrumentos.dto.UserDto;
import com.ecommerce_instrumentos.entity.User;
import com.ecommerce_instrumentos.enums.UserRole;
import com.ecommerce_instrumentos.repository.UserRepository;
import com.ecommerce_instrumentos.services.auth.AuthService;
import com.ecommerce_instrumentos.services.email.EmailService;
import com.ecommerce_instrumentos.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    private final AuthService authService;

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                          HttpServletResponse response) throws IOException, JSONException {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        }catch (BadCredentialsException e ){
            throw new BadCredentialsException("Incorrect username or password");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if(optionalUser.isPresent()){
            response.getWriter().write(new JSONObject()
                    .put("userId", optionalUser.get().getId())
                    .put("name", optionalUser.get().getName())
                    .put("lastName", optionalUser.get().getLastName())
                    .put("role", optionalUser.get().getRole())
                    .put("email", optionalUser.get().getEmail())
                    .toString()
            );
            response.addHeader("Access-Control-Expose-Headers","Authorization");
            response.addHeader("Access-Control-Allow-Headers", "Authorization X-PINGOTHER, Origin, " +
                    "X-Requested-With, Content-Type, Accept, X-Custom-header");
            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
        }
    }

    private final EmailService emailService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>("User already exist", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto userDto = authService.createUser(signupRequest);

        // Envía un correo electrónico después de un registro exitoso
        emailService.sendRegistrationSuccessEmail(signupRequest.getEmail(), signupRequest.getName());

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> listUsers() {
        List<UserDto> userDtoList = authService.listAllUsers();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }


    @GetMapping("/get-user/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        UserDto userDto = authService.getUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/change-role/{username}/{newRole}")
    public ResponseEntity<String> changeUserRole(@PathVariable String username, @PathVariable String newRole) {
        User user = userRepository.findFirstByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        UserRole role = UserRole.valueOf(newRole.toUpperCase());
        user.setRole(role);
        userRepository.save(user);

        return ResponseEntity.ok("User role updated to " + newRole);
    }




}

