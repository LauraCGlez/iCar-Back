package com.back.model;

import com.back.domain.RideStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ElementCollection
    @Column(name = "declined_driver_id") // Changed column name to avoid conflict
    private List<Integer> declinedDrivers = new ArrayList<>();

    private double pickupLatitude;

    private double pickupLongitude;

    private double destinationLatitude;

    private double destinationLongitude;

    private String pickUpArea;

    private String destinationArea;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private double fare;

    private int otp;

    private double distance;

    private long duration;

    @Embedded
    private PaymentDetails paymentDetails = new PaymentDetails();

}
