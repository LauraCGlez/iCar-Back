package com.icarapp.icar.service.impl;


import com.icarapp.icar.exception.UserAlreadyExistsException;
import com.icarapp.icar.model.Role;
import com.icarapp.icar.model.User;
import com.icarapp.icar.repository.RoleRepository;
import com.icarapp.icar.repository.UserRepository;
import com.icarapp.icar.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements com.icarapp.icar.service.UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    @Override
    public void registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        Role userRole;
        if (user.getEmail().equals("admin@icar.com")) {
            userRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_ADMIN");
                        return roleRepository.save(role);
                    });
        } else {
            userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_USER");
                        return roleRepository.save(role);
                    });
        }
        user.setRoles(Collections.singletonList(userRole));
        userRepository.save(user);

        String subject = "Welcome to iCar!";
        String text = "Hello " + user.getFirstName() + ",\n\n" +
                "Thank you for registering with iCar! We are excited to have you on board.\n\n" +
                "Best,\n" +
                "The iCar Team";
        emailService.sendConfirmationEmail(user.getEmail(), subject, text);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null){
            userRepository.deleteByEmail(email);
        }

    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
