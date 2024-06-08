package com.back.service.mapper;

import com.back.dto.DriverDTO;
import com.back.dto.RideDTO;
import com.back.dto.UserDTO;
import com.back.model.Driver;
import com.back.model.Ride;
import com.back.model.User;
import org.springframework.stereotype.Service;

@Service
public class DtoMapper {

    public static DriverDTO toDriverDTO(Driver driver) {

        DriverDTO driverDTO = new DriverDTO();

        driverDTO.setId(driver.getId());
        driverDTO.setName(driver.getName());
        driverDTO.setEmail(driver.getEmail());
        driverDTO.setMobile(driver.getMobile());
        driverDTO.setRating(driver.getRating());
        driverDTO.setLatitude(driver.getLatitude());
        driverDTO.setLongitude(driver.getLongitude());
        driverDTO.setRole(driver.getRole());
        driverDTO.setVehicle(driver.getVehicle());

        return driverDTO;
    }

    public static RideDTO toRideDTO(Ride ride) {

        DriverDTO driverDTO = toDriverDTO(ride.getDriver());

        UserDTO userDTO = toUserDTO(ride.getUser());

        RideDTO rideDTO = new RideDTO();

        rideDTO.setDestinationLatitude(ride.getDestinationLatitude());
        rideDTO.setDestinationLongitude(ride.getDestinationLongitude());
        rideDTO.setDistance(ride.getDistance());
        rideDTO.setDriver(driverDTO);
        rideDTO.setDuration(ride.getDuration());
        rideDTO.setEndTime(ride.getEndTime());
        rideDTO.setFare(ride.getFare());
        rideDTO.setId(ride.getId());
        rideDTO.setPickupLatitude(ride.getPickupLatitude());
        rideDTO.setPickupLongitude(ride.getPickupLongitude());
        rideDTO.setStartTime(ride.getStartTime());
        rideDTO.setStatus(ride.getStatus());
        rideDTO.setUser(userDTO);
        rideDTO.setPickupArea(ride.getPickUpArea());
        rideDTO.setDestinationArea(ride.getDestinationArea());
        rideDTO.setPaymentDetails(ride.getPaymentDetails());
        rideDTO.setOtp(ride.getOtp());

        return rideDTO;
    }

    public static UserDTO toUserDTO(User user){

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getFullName());
        userDTO.setMobile(user.getMobile());

        return userDTO;
    }
}
