package com.back.controller;

import com.back.exception.UserException;
import com.back.model.Ride;
import com.back.model.User;
import com.back.response.MessageResponse;
import com.back.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    public ResponseEntity<MessageResponse> homeController(){
        MessageResponse messageResponse = new MessageResponse("Welcome to the ICar API");
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<User> userHandler(@PathVariable Integer userId) throws UserException {

        User createdUser = userService.findUserById(userId);

        return new ResponseEntity<User>(createdUser, HttpStatus.ACCEPTED);

    }

    @GetMapping("/profile")
    public ResponseEntity<User> userProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserByToken(jwt);

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/rides/completed")
    public ResponseEntity<?> completedRidesHandler(@RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserByToken(jwt);

        List<Ride> rides = userService.completedRides(user.getId());

        return new ResponseEntity<>(rides, HttpStatus.ACCEPTED);
    }

}
