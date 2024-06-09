package com.back.model;

import com.back.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String email;

    private String name;

    private String mobile;

    private double rating;

    private double latitude;

    private double longitude;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "license", referencedColumnName = "license_number")
    private License license;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rides")
    private List<Ride> rides;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    private Integer totalRevenue=0;

}
