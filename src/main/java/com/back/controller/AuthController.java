package com.back.controller;

import com.back.exception.UserException;
import com.back.model.User;
import com.back.repository.UserRepository;
import com.back.repository.DriverRepository;
import com.back.request.SignupRequest;
import com.back.response.JwtResponse;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public class AuthController {

    private UserRepository userRepository;

    private DriverRepository driverRepository;

    public AuthController(UserRepository userRepository, DriverRepository driverRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
    }

    public ResponseEntity<JwtResponse> signupHandler(@RequestBody SignupRequest request) throws UserException {

        String email = request.getEmail();
        String fullName = request.getFullName();
        String password = request.getPassword();
        String mobile = request.getMobile();

        User user = userRepository.findByEmail(email);

        if (user != null) {
            throw new UserException("User already exists");
        }

        return null;
    }

}
