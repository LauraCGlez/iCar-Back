package com.back.service.impl;

import com.back.domain.RideStatus;
import com.back.exception.DriverException;
import com.back.exception.RideException;
import com.back.model.Driver;
import com.back.model.Notification;
import com.back.model.Ride;
import com.back.model.User;
import com.back.repository.DriverRepository;
import com.back.repository.NotificationRepository;
import com.back.repository.RideRepository;
import com.back.request.RideRequest;
import com.back.service.Calculaters;
import com.back.service.DriverService;
import org.springframework.stereotype.Service;
import com.back.service.RideService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class RideServiceImpl implements RideService{

    private final DriverService driverService;

    private final RideRepository rideRepository;

    private final Calculaters calculaters;

    private final DriverRepository driverRepository;

    private final NotificationRepository notificationRepository;

    public RideServiceImpl(DriverService driverService, RideRepository rideRepository, Calculaters calculaters, DriverRepository driverRepository, NotificationRepository notificationRepository) {
        this.driverService = driverService;
        this.rideRepository = rideRepository;
        this.calculaters = calculaters;
        this.driverRepository = driverRepository;
        this.notificationRepository = notificationRepository;
    }


    @Override
    public Ride requestRide(RideRequest rideRequest, User user) throws DriverException {

        double pickupLatitude = rideRequest.getPickupLatitude();
        double pickupLongitude = rideRequest.getPickupLongitude();
        double destinationLatitude = rideRequest.getDestinationLatitude();
        double destinationLongitude = rideRequest.getDestinationLongitude();

        String pickupArea = rideRequest.getPickupAddress();
        String destinationArea = rideRequest.getDestinationArea();

        Ride existingRide = new Ride();

        List<Driver> availableDrivers = driverService.getAvailableDrivers(pickupLatitude, pickupLongitude, 5, existingRide);

        Driver nearestDriver = driverService.findNearestDriver(availableDrivers, pickupLatitude, pickupLongitude);

        if (nearestDriver == null) {
            throw new DriverException("No drivers available");
        }

        Ride ride = createRideRequest(user, nearestDriver, pickupLatitude,
                pickupLongitude, destinationLatitude, destinationLongitude,
                pickupArea, destinationArea);

        Notification notification = new Notification();

        notification.setDriver(nearestDriver);
        notification.setMesssage("New ride request");
        notifiaction.setRide(ride);
        notification.setTimestamp(LocalDateTime.now());
        notification.setType(NotificationType.RIDE_REQUEST);

        Notification savedNotification = notificationRepository.save(notification);

        //rideService.sendNotificationToDriver(nearestDriver, ride);

        return ride;
    }

    @Override
    public Ride createRideRequest(User user, Driver nearestDriver, double pickupLatitude, double pickupLongitude, double destinationLatitude, double destinationLongitude, String pickupArea, String destinationArea) {

        Ride ride = new Ride();

        ride.setDriver(nearestDriver);
        ride.setUser(user);
        ride.setPickupLatitude(pickupLatitude);
        ride.setPickupLongitude(pickupLongitude);
        ride.setDestinationLatitude(destinationLatitude);
        ride.setDestinationLongitude(destinationLongitude);
        ride.setStatus(RideStatus.REQUESTED);
        ride.setPickUpArea(pickupArea);
        ride.setDestinationArea(destinationArea);

        return rideRepository.save(ride);
    }

    @Override
    public void acceptRide(Integer rideId) throws RideException {

        Ride ride = findRideById(rideId);

        ride.setStatus(RideStatus.ACCEPTED);

        Driver driver = ride.getDriver();

        driver.setCurrentRide(ride);

        Random random = new Random();

        int otp = random.nextInt(9000) + 1000;

        ride.setOtp(otp);

        driverRepository.save(driver);

        rideRepository.save(ride);

        Notification notification = new Notification();

        notification.setUser(ride.getUser());

        notification.setMesssage("Your ride has been accepted");

        notification.setRide(ride);

        notification.setTimestamp(LocalDateTime.now());

        notification.setType(NotificationType.RIDE_CONFIRMATION);

        Notification savedNotification = notificationRepository.save(notification);
    }

    @Override
    public void startRide(Integer rideId, int opt) throws RideException {

        Ride ride = findRideById(rideId);

        if (opt != ride.getOtp()) {
            throw new RideException("Invalid OTP");
        }

        ride.setStatus(RideStatus.STARTED);
        ride.setStartTime(LocalDateTime.now());
        rideRepository.save(ride);

        Notification notification = new Notification();

        notification.setUser(ride.getUser());
        notification.setMesssage("Drive reached at your location");
        notification.setRide(ride);
        notification.setTimestamp(LocalDateTime.now());
        notification.setType(NotificationType.RIDE_CONFIRMATION);

        Notification savedNotification = notificationRepository.save(notification);
    }

    @Override
    public void declineRide(Integer rideId, Integer driveId) throws RideException {

        Ride ride = findRideById(rideId);

        ride.getDeclinedDrivers().add(driveId);

        List<Driver> availableDrivers = driverService.getAvailableDrivers(ride.getPickupLatitude(),
                ride.getPickupLongitude(), 5, ride);

        Driver nearestDriver = driverService.findNearestDriver(availableDrivers, ride.getPickupLatitude(),
                ride.getPickupLongitude());

        ride.setDriver(nearestDriver);

        rideRepository.save(ride);

    }

    @Override
    public void completeRide(Integer rideId) throws RideException {

        Ride ride = findRideById(rideId);

        ride.setStatus(RideStatus.COMPLETED);
        ride.setEndTime(LocalDateTime.now());

        double distance = calculaters.calculateDistance(ride.getPickupLatitude(), ride.getPickupLongitude(), ride.getDestinationLatitude(), ride.getDestinationLongitude());

        LocalDateTime start = ride.getStartTime();
        LocalDateTime end = ride.getEndTime();

        Duration duration = Duration.between(start, end);

        long milliSecounds = duration.toMillis();

        double fare = calculaters.calculateFare(distance);

        ride.setDistance(Math.round(distance * 100.0) / 100.0);
        ride.setFare((int) Math.round(fare));
        ride.setDuration(milliSecounds);
        ride.setEndTime(LocalDateTime.now());

        Driver driver = ride.getDriver();

        driver.getRides().add(ride);
        driver.setCurrentRide(null);

        Integer driversRenue = (int) (driver.getTotalRevenue() + Math.round(fare * 0.8));

        driverRepository.save(driver);
        rideRepository.save(ride);

        Notification notification = new Notification();

        notification.setUser(ride.getUser());
        notification.setMesssage("Driver reached at your location");
        notification.setRide(ride);
        notification.setTimestamp(LocalDateTime.now());
        notification.setType(NotificationType.RIDE_CONFIRMATION);

        Notification savedNotification = notificationRepository.save(notification);
    }

    @Override
    public void cancelRide(Integer rideId) throws RideException {

        Ride ride = findRideById(rideId);

        ride.setStatus(RideStatus.CANCELLED);

        rideRepository.save(ride);

    }

    @Override
    public Ride findRideById(Integer rideId) throws RideException {

        Optional<Ride> ride = rideRepository.findById(rideId);

        if (ride.isPresent()) {
            return ride.get();
        }
        throw new RideException("Ride not found");
    }
}
