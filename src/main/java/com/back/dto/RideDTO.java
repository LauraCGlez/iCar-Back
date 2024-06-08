package com.back.dto;

import com.back.domain.RideStatus;
import com.back.model.PaymentDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideDTO {

    private Integer id;

    private UserDTO user;

    private DriverDTO driver;

    private double pickupLatitude;

    private double pickupLongitude;

    private double destinationLatitude;

    private double destinationLongitude;

    private String pickupArea;

    private String destinationArea;

    private double distance;

    private long duration;

    private RideStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private double fare;

    private PaymentDetails paymentDetails;

    private int otp;

}
