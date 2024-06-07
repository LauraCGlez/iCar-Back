package com.back.model;

import com.back.domain.RideStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private User user;

    @JsonIgnore
    private List<Integer> declinedDrivers = new ArrayList<>();

    private double pickulLatitude;

    private double pickulLongitude;

    private double destinationLatitude;

    private double destinationLongitude;

    private String pickUpArea;

    private String destinationArea;

    private RideStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private double fare;

    private int otp;

    @Embedded
    private PaymentDetails paymentDetails = new PaymentDetails();

}
