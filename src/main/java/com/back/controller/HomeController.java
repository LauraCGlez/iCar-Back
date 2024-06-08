package com.back.controller;

import com.back.config.JwtUtil;
import com.back.exception.UserException;
import com.back.model.Driver;
import com.back.repository.DriverRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    private final DriverRepository driverRepository;

    private final JwtUtil jwtUtil;

    public HomeController(DriverRepository driverRepository, JwtUtil jwtUtil) {
        this.driverRepository = driverRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/api/profile")
    public ResponseEntity<?> userProfilerHandler(@RequestHeader("Authorization") String jwt) throws UserException {

        String email = jwtUtil.getEmailFromJwtToken(jwt);

        if (email == null) {
            throw new UserException("Invalid Token");
        }

        Driver driver = driverRepository.findByEmail(email);


        return ResponseEntity.ok("User Profile");
    }

}
