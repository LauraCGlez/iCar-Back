package com.back.service;

import com.back.exception.DriverException;
import com.back.model.Driver;
import com.back.model.Ride;
import com.back.request.DriverSignupRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DriverService {

    Driver registerDriver(DriverSignupRequest request);

    List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude, double radius, Ride ride);

    Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude);

    Driver getReqDriverProfile(String jwt) throws DriverException;

    Ride getDriversCurrentRide(Integer driverId) throws DriverException;

    List<Ride> getAllocatedRides(Integer dreiverId) throws DriverException;

    Driver findDriverById(Integer driverId) throws DriverException;

    List<Ride> completedRides(Integer driverId) throws DriverException;

}
