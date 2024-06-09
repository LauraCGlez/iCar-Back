package com.back.controller;

import com.back.exception.DriverException;
import com.back.model.Driver;
import com.back.model.Ride;
import com.back.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping("/profile")
    public ResponseEntity<Driver> getReqDriverProfile(@RequestHeader("Authorization") String jwt) throws DriverException {

        Driver driver = driverService.getReqDriverProfile(jwt);

        return new ResponseEntity<>(driver, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{driverId}/allocated")
    public ResponseEntity<List<Ride>> getAllocatedRides(@PathVariable Integer driverId) throws DriverException {

        List<Ride> rides = driverService.getAllocatedRides(driverId);

        return new ResponseEntity<>(rides, HttpStatus.ACCEPTED);
    }

    @GetMapping("/rides/completed")
    public ResponseEntity<List<Ride>> getCompletedRides(@RequestHeader("Authorization") String jwt) throws DriverException {

        Driver driver = driverService.getReqDriverProfile(jwt);

        List<Ride> rides = driverService.completedRides(driver.getId());

        return new ResponseEntity<>(rides, HttpStatus.ACCEPTED);
    }

}
