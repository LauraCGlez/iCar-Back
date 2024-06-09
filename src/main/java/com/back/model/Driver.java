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

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
    private License license;

    @JsonIgnore
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<Ride> rides;

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
    private Vehicle vehicle;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Ride currentRide;

    private Integer totalRevenue=0;

}
