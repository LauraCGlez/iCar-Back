package com.icarapp.icar.service.impl;

import com.icarapp.icar.model.Car;
import com.icarapp.icar.model.CarType;
import com.icarapp.icar.service.CarService;
import com.icarapp.icar.exception.InternalServerException;
import com.icarapp.icar.exception.ResourceNotFoundException;
import com.icarapp.icar.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Override
    public Car addNewCar(CarType carType, BigDecimal carPrice) throws SQLException, IOException {
        Car car = new Car();
        car.setCarType(carType);
        car.setCarPrice(carPrice);
        return carRepository.save(car);
    }

    @Override
    public List<CarType> getAllCarTypes() {
        return carRepository.findDistinctCarTypes();
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public void deleteCar(Long carId) {
        Optional<Car> theCar = carRepository.findById(carId);
        if(theCar.isPresent()){
            carRepository.deleteById(carId);
        }
    }

    @Override
    public Car updateCar(Long carId, CarType carType, BigDecimal carPrice) {
        Car car = carRepository.findById(carId).get();
        if (carType != null) car.setCarType(carType);
        if (carPrice != null) car.setCarPrice(carPrice);

        return carRepository.save(car);
    }

    @Override
    public Optional<Car> getCarById(Long carId) {
        return Optional.of(carRepository.findById(carId).get());
    }

    @Override
    public List<Car> getAvailableCars(LocalDate checkInDate, LocalDate checkOutDate, CarType carType) {
        return carRepository.findAvailableCarsByDatesAndType(checkInDate, checkOutDate, carType);
    }

}
