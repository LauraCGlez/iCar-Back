package com.back.request;

import com.back.model.License;
import com.back.model.Vehicle;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverSignupRequest {

    private String email;

    private String name;

    private String password;

    private String mobile;

    private double latitude;

    private double longitude;

    @OneToOne(mappedBy = "driver")
    private License license;

    @OneToOne(mappedBy = "driver")
    private Vehicle vehicle;

}
