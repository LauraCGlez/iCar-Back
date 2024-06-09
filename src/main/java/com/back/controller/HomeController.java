package com.back.controller;

import com.back.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<MessageResponse> homeController() {
        MessageResponse messageResponse = new MessageResponse("Welcome to iCar");
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.ACCEPTED);
    }



}
