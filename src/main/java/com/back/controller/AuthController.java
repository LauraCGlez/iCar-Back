package com.back.controller;

import com.back.config.JwtUtil;
import com.back.domain.UserRole;
import com.back.exception.UserException;
import com.back.model.Driver;
import com.back.model.User;
import com.back.repository.DriverRepository;
import com.back.repository.UserRepository;
import com.back.request.DriverSignupRequest;
import com.back.request.LoginRequest;
import com.back.request.SignupRequest;
import com.back.response.JwtResponse;
import com.back.service.CustomeUserDetailsService;
import com.back.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    private final DriverRepository driverRepository;

    @Autowired
    private DriverService driverService;

    private final PasswordEncoder passwordEncoder;

    private final CustomeUserDetailsService customeUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, DriverRepository driverRepository, PasswordEncoder passwordEncoder, CustomeUserDetailsService customeUserDetailsService) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
        this.customeUserDetailsService = customeUserDetailsService;
    }

    //api/auth/user/signup
    @PostMapping("/user/signup")
    public ResponseEntity<JwtResponse> signupHandler(@RequestBody SignupRequest request) throws UserException {

        User user = userRepository.findByEmail(request.getEmail());

        if (user != null) {
            throw new UserException("User already exists");
        }

        String encodePassword = passwordEncoder.encode(request.getPassword());

        //Create new user
        User newUser = new User();

        newUser.setEmail(request.getEmail());

        newUser.setFullName(request.getFullName());

        newUser.setPassword(encodePassword);

        newUser.setMobile(request.getMobile());

        newUser.setRole(UserRole.USER);

        User savedUser = userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Generate JWT token
        String token = jwtUtil.generateJwtToken(authentication);

        JwtResponse jwtResponse = new JwtResponse();

        jwtResponse.setJwt(token);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setType(UserRole.USER);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setMessage("User registered successfully");

        return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.OK);
    }

    //api/auth/driver/signup
    @PostMapping("/driver/signup")
    public ResponseEntity<JwtResponse> driverSignupHandler(@RequestBody DriverSignupRequest request) {

        Driver driver = driverRepository.findByEmail(request.getEmail());

        JwtResponse jwtResponse = new JwtResponse();

        if (driver != null) {

            jwtResponse.setAuthenticated(false);
            jwtResponse.setError(true);
            jwtResponse.setErrorDetails("Driver already exists");

            return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.BAD_REQUEST);
        }

        Driver savedDriver = driverService.registerDriver(request);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedDriver.getEmail(), savedDriver.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Generate JWT token
        String token = jwtUtil.generateJwtToken(authentication);

        jwtResponse.setJwt(token);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setType(UserRole.DRIVER);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setMessage("Driver registered successfully");

        return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.OK);

    }

    //api/auth/signin
    @PostMapping("signin")
    public ResponseEntity<JwtResponse> signinHandler(@RequestBody LoginRequest request) {

        String username = request.getEmail();

        String password = request.getPassword();

        Authentication authentication = authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateJwtToken(authentication);

        JwtResponse jwtResponse = new JwtResponse();

        jwtResponse.setJwt(token);

        jwtResponse.setAuthenticated(true);

        jwtResponse.setError(false);

        jwtResponse.setErrorDetails(null);

        jwtResponse.setMessage("Account Created successfully");

        return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.OK);
    }


    private Authentication authenticate(String username, String password) {

        System.out.println("Sign in userDetails - " + password);

        UserDetails userDetails = customeUserDetailsService.loadUserByUsername(username);

        System.out.println("UserDetails after load - " + userDetails.getPassword());

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
