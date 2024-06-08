package com.back.service;

import com.back.exception.UserException;
import com.back.model.Ride;
import com.back.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user) throws UserException;

    User getReqUserProfile(String token) throws UserException;

    User findUserById(Integer id) throws UserException;

    User findUserByEmail(String email) throws UserException;

    User findUserByToken(String token) throws UserException;

    List<Ride> completedRides(Integer userId) throws UserException;
}
