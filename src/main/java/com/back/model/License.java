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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String licenseNumber;

    private String licenseState;

    private String licenseExpirationDate;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Driver driver;
}
