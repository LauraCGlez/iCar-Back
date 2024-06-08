package com.back.controller;

import com.back.dto.RideDTO;
import com.back.exception.DriverException;
import com.back.exception.RideException;
import com.back.exception.UserException;
import com.back.model.Driver;
import com.back.request.StartRideRequest;
import com.back.service.mapper.DtoMapper;
import com.back.model.Ride;
import com.back.model.User;
import com.back.request.RideRequest;
import com.back.response.MessageResponse;
import com.back.service.DriverService;
import com.back.service.RideService;
import com.back.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    private final UserService userService;

    private final DriverService driverService;

    public RideController(RideService rideService, UserService userService, DriverService driverService) {
        this.rideService = rideService;
        this.userService = userService;
        this.driverService = driverService;
    }

    @PostMapping("/request")
    public ResponseEntity<RideDTO> userRequestRideHandler(@RequestBody RideRequest rideRequest, @RequestHeader("Authorization") String token) throws UserException, RideException, DriverException {

        User user = userService.findUserByToken(token);

        Ride ride = rideService.requestRide(rideRequest, user);

        RideDTO rideDTO = DtoMapper.toRideDTO(ride);

        return new ResponseEntity<>(rideDTO, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/accept")
    public ResponseEntity<MessageResponse> acceptRideHandler(@PathVariable Integer rideId) throws UserException, RideException {

        rideService.acceptRide(rideId);

        MessageResponse messageResponse = new MessageResponse("Ride Accepted");

        return new ResponseEntity<>(messageResponse, HttpStatus.ACCEPTED);


    }

    @PutMapping("/{rideId}/decline")
    public ResponseEntity<MessageResponse> declineRideHandler(@RequestHeader("Authorization") String jwt, @PathVariable Integer rideId)
            throws RideException, DriverException {

        Driver driver = driverService.getReqDriverProfile(jwt);

        rideService.declineRide(rideId, driver.getId());

        MessageResponse messageResponse = new MessageResponse("Ride Declined");

        return new ResponseEntity<>(messageResponse, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/start")
    public ResponseEntity<MessageResponse> startRideHandler(@PathVariable Integer rideId, @RequestBody StartRideRequest request)
            throws RideException {

        rideService.startRide(rideId, request.getOpt());

        MessageResponse messageResponse = new MessageResponse("Ride Started");

        return new ResponseEntity<>(messageResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<RideDTO> getRideHandler(@PathVariable Integer rideId, @RequestHeader("Authorization") String jwt)
            throws RideException, UserException {

        User user = userService.findUserByToken(jwt);

        Ride ride = rideService.findRideById(rideId);

        RideDTO rideDTO = DtoMapper.toRideDTO(ride);

        return new ResponseEntity<RideDTO>(rideDTO, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{rideId}/complete")
    public ResponseEntity<MessageResponse> completeRideHandler(@PathVariable Integer rideId)
            throws RideException, UserException {

        rideService.completeRide(rideId);

        MessageResponse messageResponse = new MessageResponse("Ride Completed");

        return new ResponseEntity<>(messageResponse, HttpStatus.ACCEPTED);
    }
}
