package com.icarapp.icar.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CarType {
    SUV,
    SEDAN,
    HATCHBACK;

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
