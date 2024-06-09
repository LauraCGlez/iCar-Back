package com.back.controller;

import com.back.exception.UserException;
import com.back.model.Ride;
import com.back.model.User;
import com.back.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{userId}")
    public ResponseEntity<User> findUserByIdHandler(@PathVariable Integer userId) throws UserException {

        User createdUser = userService.findUserById(userId);

        return new ResponseEntity<User>(createdUser, HttpStatus.ACCEPTED);

    }

    @GetMapping("/profile")
    public ResponseEntity<User> getReqUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.getReqUserProfile(jwt);

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/rides/completed")
    public ResponseEntity<List<Ride>> completedRidesHandler(@RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserByToken(jwt);

        List<Ride> rides = userService.completedRides(user.getId());

        return new ResponseEntity<>(rides, HttpStatus.ACCEPTED);
    }

}
