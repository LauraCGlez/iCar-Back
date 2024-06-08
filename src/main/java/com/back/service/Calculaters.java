package com.back.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class Calculaters {

    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    public double calculateDistance(double sourceLat, double sourceLon, double destLat, double destLon) {
        double dLat = Math.toRadians(destLat - sourceLat);
        double dLon = Math.toRadians(destLon - sourceLon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(sourceLat)) * Math.cos(Math.toRadians(destLat)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }

    public long calculateDuration(LocalDateTime starTime, LocalDateTime endTime) {
        Duration duration = Duration.between(starTime, endTime);
        return duration.getSeconds();
    }

    public double calculateFare(double distance) {
        double baseFare = 11;
        return baseFare * distance;
    }

}
