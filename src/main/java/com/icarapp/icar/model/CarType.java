package com.icarapp.icar.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CarType {
    ECONOMY,
    COMPACT,
    INTERMEDIATE,
    STANDARD,
    FULL_SIZE,
    PREMIUM,
    LUXURY,
    CONVERTIBLE,
    MINIVAN,
    SUV;

    @JsonCreator
    public static CarType fromString(String value) {
        for (CarType carType : CarType.values()) {
            if (carType.name().equalsIgnoreCase(value)) {
                return carType;
            }
        }
        throw new IllegalArgumentException("Invalid car type: " + value);
    }
}
