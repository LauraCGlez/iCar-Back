package com.back.service.impl;

import com.back.exception.RideException;
import com.back.model.Driver;
import com.back.model.Ride;
import com.back.model.User;
import com.back.request.RideRequest;
import org.springframework.stereotype.Service;
import com.back.service.RideService;

@Service
public class RideServiceImpl implements RideService{


    @Override
    public Ride requestRide(RideRequest rideRequest, User user) throws RideException {
        return null;
    }

    @Override
    public Ride createRideRequest(User user, Driver nearestDriver, double pickupLatitude, double pickupLongitude, double destinationLatitude, double destinationLongitude, String pickupArea, String destinationArea) {
        return null;
    }

    @Override
    public void acceptRide(Integer rideId) throws RideException {

    }

    @Override
    public void startRide(Integer rideId, int opt) throws RideException {

    }

    @Override
    public void declineRide(Integer rideId, Integer driveId) throws RideException {

    }

    @Override
    public void completeRide(Integer rideId) throws RideException {

    }

    @Override
    public void cancelRide(Integer rideId) throws RideException {

    }

    @Override
    public Ride findRideById(Integer rideId) throws RideException {
        return null;
    }
}
