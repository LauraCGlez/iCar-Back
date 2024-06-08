package com.back.dto;

import com.back.domain.UserRole;
import com.back.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDTO {

    private Integer id;

    private String name;

    private String email;

    private String mobile;

    private double rating;

    private double latitude;

    private double longitude;

    private UserRole role;

    private Vehicle vehicle;

}
