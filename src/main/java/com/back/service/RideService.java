package com.back.service;

import com.back.exception.DriverException;
import com.back.exception.RideException;
import com.back.model.Driver;
import com.back.model.Ride;
import com.back.model.User;
import com.back.request.RideRequest;
import org.springframework.stereotype.Service;

@Service
public interface RideService {

    Ride requestRide(RideRequest rideRequest, User user) throws RideException, DriverException;

    Ride createRideRequest(User user, Driver nearestDriver, double pickupLatitude,
                                  double pickupLongitude, double destinationLatitude,
                                  double destinationLongitude, String pickupArea, String destinationArea);

    void acceptRide(Integer rideId) throws RideException;

    void startRide(Integer rideId, int opt) throws RideException;

    void declineRide(Integer rideId, Integer driveId) throws RideException;

    void completeRide(Integer rideId) throws RideException;

    void cancelRide(Integer rideId) throws RideException;

    Ride findRideById(Integer rideId) throws RideException;
}
