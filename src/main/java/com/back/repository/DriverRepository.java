package com.back.repository;

import com.back.model.Driver;
import com.back.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Integer> {

    Driver findByEmail(String email);

    @Query("select r from Ride r where r.status = 'REQUESTED' and r.driver.id = :driverId")
    List<Ride> getAllocatedRides(@Param("driverId") Integer driverId);

    @Query("select r from Ride r where r.status = 'COMPLETED' and r.driver.id = :driverId")
    List<Ride> getCompletedRides(@Param("driverId") Integer driverId);
}
