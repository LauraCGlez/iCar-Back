package com.icarapp.icar.repository;


import com.icarapp.icar.model.Car;
import com.icarapp.icar.model.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT DISTINCT r.carType FROM Car r")
    List<CarType> findDistinctCarTypes();

    @Query(" SELECT r FROM Car r " +
            " WHERE r.carType = :carType " +
            " AND r.id NOT IN (" +
            "  SELECT br.car.id FROM BookedCar br " +
            "  WHERE ((br.checkInDate <= :checkOutDate) AND (br.checkOutDate >= :checkInDate))" +
            ")")
    List<Car> findAvailableCarsByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, CarType carType);
}

