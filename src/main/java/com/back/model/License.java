package com.back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class License {

    @Id
    @Column(name = "license_number", unique = true, nullable = false)
    private String licenseNumber;

    private String licenseState;

    private String licenseExpirationDate;

    @OneToOne(mappedBy = "license")
    private Driver driver;

}
