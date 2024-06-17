package com.icarapp.icar.service;

import com.icarapp.icar.model.Car;
import com.icarapp.icar.model.CarType;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



public interface CarService {
    Car addNewCar(CarType carType, BigDecimal roomPrice) throws SQLException, IOException;

    List<CarType> getAllCarTypes();

    List<Car> getAllCars();

    void deleteCar(Long roomId);

    Car updateCar(Long roomId, CarType carType, BigDecimal roomPrice);

    Optional<Car> getCarById(Long roomId);

    List<Car> getAvailableCars(LocalDate checkInDate, LocalDate checkOutDate, CarType carType);
}
