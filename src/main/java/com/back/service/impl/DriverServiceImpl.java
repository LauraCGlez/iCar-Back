package com.back.service.impl;

import com.back.config.JwtUtil;
import com.back.domain.RideStatus;
import com.back.domain.UserRole;
import com.back.exception.DriverException;
import com.back.model.Driver;
import com.back.model.License;
import com.back.model.Ride;
import com.back.model.Vehicle;
import com.back.repository.DriverRepository;
import com.back.repository.LicenseRepository;
import com.back.repository.RideRepository;
import com.back.repository.VehicleRepository;
import com.back.request.DriverSignupRequest;
import com.back.service.DriverService;
import com.back.service.Calculaters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private Calculaters calculaters;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private RideRepository rideRepository;

    @Override
    public List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude, double radius, Ride ride) {

        List<Driver> allDrivers = driverRepository.findAll();

        List<Driver> availableDrivers = new ArrayList<>();

        for (Driver driver : allDrivers) {

            if(driver.getCurrentRide() != null && driver.getCurrentRide().getStatus() != RideStatus.COMPLETED) {
                continue;
            }

            if (ride.getDeclinedDrivers().contains(driver.getId())) {
                continue;
            }

            double driverLatitude = driver.getLatitude();

            double driverLongitude = driver.getLongitude();

            double distance = calculaters.calculateDistance(driverLatitude, driverLongitude, pickupLatitude, pickupLongitude);

            if (distance <= radius) {
                availableDrivers.add(driver);
            }
        }
        return availableDrivers;
    }

    @Override
    public Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude) {
        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;

        //List<Driver> drivers = new ArrayList<>();
        //double minAutoDistance = Double.MAX_VALUE;

        for (Driver driver : availableDrivers) {
            double driverLatitude = driver.getLatitude();
            double driverLongitude = driver.getLongitude();
            double distance = calculaters.calculateDistance(pickupLatitude, pickupLongitude, driverLatitude, driverLongitude);

            if (distance < minDistance) {
                minDistance = distance;
                nearestDriver = driver;
            }
        }
        return nearestDriver;
    }

    @Override
    public Driver registerDriver(DriverSignupRequest request) {

        License license = request.getLicense();
        Vehicle vehicle = request.getVehicle();

        License createdLicense = new License();

        createdLicense.setLicenseState(license.getLicenseState());
        createdLicense.setLicenseNumber(license.getLicenseNumber());
        createdLicense.setLicenseExpirationDate(license.getLicenseExpirationDate());
        createdLicense.setId(license.getId());

        License savedLicense = licenseRepository.save(createdLicense);

        Vehicle createdVehicle = new Vehicle();

        createdVehicle.setCapacity(vehicle.getCapacity());
        createdVehicle.setMake(vehicle.getMake());
        createdVehicle.setModel(vehicle.getModel());
        createdVehicle.setYear(vehicle.getYear());
        createdVehicle.setLicensePlate(vehicle.getLicensePlate());
        createdVehicle.setId(vehicle.getId());
        createdVehicle.setColor(vehicle.getColor());

        Vehicle savedVehicle = vehicleRepository.save(createdVehicle);

        Driver driver = new Driver();

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        driver.setEmail(request.getEmail());
        driver.setName(request.getName());
        driver.setMobile(request.getMobile());
        driver.setPassword(encodedPassword);
        driver.setLatitude(request.getLatitude());
        driver.setLongitude(request.getLongitude());
        driver.setLicense(savedLicense);
        driver.setVehicle(savedVehicle);
        driver.setRole(UserRole.DRIVER);

        Driver createdDriver = driverRepository.save(driver);

        createdLicense.setDriver(createdDriver);
        createdVehicle.setDriver(createdDriver);

        licenseRepository.save(createdLicense);
        vehicleRepository.save(createdVehicle);

        return createdDriver;

    }

    @Override
    public Driver getReqDriverProfile(String jwt) throws DriverException {

        String email = jwtUtil.getEmailFromJwtToken(jwt);

        Driver driver = driverRepository.findByEmail(email);

        if (driver == null) {
            throw new DriverException("Driver not found");
        }
        return driver;
    }

    @Override
    public Ride getDriversCurrentRide(Integer driverId) throws DriverException {

        Driver driver = findDriverById(driverId);

        return driver.getCurrentRide();
    }

    @Override
    public List<Ride> getAllocatedRides(Integer dreiverId) throws DriverException {

        return driverRepository.getAllocatedRides(dreiverId);
    }

    @Override
    public Driver findDriverById(Integer driverId) throws DriverException {

        Optional<Driver> driver = driverRepository.findById(driverId);

        if (driver.isPresent()) {
            return driver.get();
        }

        throw new DriverException("Driver not found");
    }

    @Override
    public List<Ride> completedRides(Integer driverId) throws DriverException {

        return driverRepository.getCompletedRides(driverId);
    }


}
