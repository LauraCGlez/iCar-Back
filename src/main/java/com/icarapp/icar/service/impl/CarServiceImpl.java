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
    public Car addNewCar(CarType carType, BigDecimal roomPrice) throws SQLException, IOException {
        Car car = new Car();
        car.setCarType(carType);
        car.setCarPrice(roomPrice);
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
    public void deleteRoom(Long roomId) {
        Optional<Car> theRoom = carRepository.findById(roomId);
        if(theRoom.isPresent()){
            carRepository.deleteById(roomId);
        }
    }

    @Override
    public Car updateCar(Long roomId, CarType carType, BigDecimal roomPrice) {
        Car car = carRepository.findById(roomId).get();
        if (carType != null) car.setCarType(carType);
        if (roomPrice != null) car.setCarPrice(roomPrice);

       return carRepository.save(car);
    }

    @Override
    public Optional<Car> getCarById(Long roomId) {
        return Optional.of(carRepository.findById(roomId).get());
    }

    @Override
    public List<Car> getAvailableCars(LocalDate checkInDate, LocalDate checkOutDate, CarType carType) {
        return carRepository.findAvailableCarsByDatesAndType(checkInDate, checkOutDate, carType);
    }

    private byte[] getDefaultImage(String roomType) throws IOException {
        Path dir = Paths.get("src/main/resources/img" + roomType);
        try (Stream<Path> paths = Files.list(dir)) {
            Path[] files = paths.toArray(Path[]::new);
            if (files.length > 0) {
                Path file = files[0];
                return Files.readAllBytes(file);
            } else {
                throw new IOException("No default image found for room type " + roomType);
            }
        }
    }
}
