package com.icarapp.icar.service;

import java.util.List;

import com.icarapp.icar.model.User;


public interface UserService {
    void registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
}
