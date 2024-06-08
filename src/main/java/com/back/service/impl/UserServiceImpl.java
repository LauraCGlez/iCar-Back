package com.back.service.impl;

import com.back.config.JwtUtil;
import com.back.exception.UserException;
import com.back.model.Ride;
import com.back.model.User;
import com.back.repository.UserRepository;
import com.back.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public User createUser(User user) throws UserException {

        User emailExit = findUserByEmail(user.getEmail());

        if (emailExit != null) {
            throw new UserException("Email already exist");
        }

        return userRepository.save(user);
    }

    @Override
    public User getReqUserProfile(String token) throws UserException {

        String email = jwtUtil.getEmailFromJwtToken(token);

        User user = userRepository.findByEmail(email);

        if (user != null) {
            return user;
        } else {
            throw new UserException("Invalid token");
        }

    }

    @Override
    public User findUserById(Integer id) throws UserException {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserException("User not found");
        }
    }

    @Override
    public User findUserByEmail(String email) throws UserException {

        User user = userRepository.findByEmail(email);

        if (user != null) {
            return user;
        } else {
            throw new UserException("User not found");
        }
    }

    @Override
    public User findUserByToken(String token) throws UserException {

        String email = jwtUtil.getEmailFromJwtToken(token);

        if (email == null) {
            throw new UserException("Invalid token");
        }

        User user = userRepository.findByEmail(email);

        if (user != null) {
            return user;
        } else {
            throw new UserException("User not found");
        }

    }

    @Override
    public List<Ride> completedRides(Integer userId) throws UserException {

        return userRepository.getCompletedRides(userId);

    }

}
