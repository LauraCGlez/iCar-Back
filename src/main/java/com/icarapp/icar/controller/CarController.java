package com.icarapp.icar.controller;

import com.icarapp.icar.exception.ResourceNotFoundException;
import com.icarapp.icar.model.BookedCar;
import com.icarapp.icar.model.Car;
import com.icarapp.icar.model.CarType;
import com.icarapp.icar.response.BookingResponse;
import com.icarapp.icar.response.CarResponse;
import com.icarapp.icar.service.CarService;
import com.icarapp.icar.service.impl.BookingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;
    private final BookingServiceImpl bookingServiceImpl;

    @PostMapping("/add/new-car")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CarResponse> addNewCar(
            @RequestParam("carType") CarType carType,
            @RequestParam("carPrice") BigDecimal carPrice) throws SQLException, IOException {
        Car savedCar = carService.addNewCar(carType, carPrice);
        CarResponse response = new CarResponse(savedCar.getId(), savedCar.getCarType(),
                savedCar.getCarPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/car/types")
    public List<CarType> getCarTypes() {
        return carService.getAllCarTypes();
    }

    @GetMapping("/all-cars")
    public ResponseEntity<List<CarResponse>> getAllCars() throws SQLException {
        List<Car> cars = carService.getAllCars();
        List<CarResponse> carResponse = new ArrayList<>();
        for (Car car : cars) {
            carResponse.add(getCarResponse(car));
        }
        return ResponseEntity.ok(carResponse);
    }

    @DeleteMapping("/delete/car/{carId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCar(@PathVariable Long carId) {
        carService.deleteCar(carId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{carId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long carId,
                                                 @RequestParam(required = false) CarType carType,
                                                 @RequestParam(required = false) BigDecimal roomPrice ) {

        Car theCar = carService.updateCar(carId, carType, roomPrice);
        CarResponse carResponse = getCarResponse(theCar);
        return ResponseEntity.ok(carResponse);
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<Optional<CarResponse>> getCarById(@PathVariable Long carId) {
        Optional<Car> theCar = carService.getCarById(carId);
        return theCar.map(car -> {
            CarResponse carResponse = getCarResponse(car);
            return ResponseEntity.ok(Optional.of(carResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @GetMapping("/available-cars")
    public ResponseEntity<List<CarResponse>> getAvailableCars(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam("carType") CarType carType) throws SQLException {
        List<Car> availableCars = carService.getAvailableCars(checkInDate, checkOutDate, carType);
        List<CarResponse> carRespons = new ArrayList<>();
        for (Car car : availableCars) {
            CarResponse carResponse = getCarResponse(car);
            carRespons.add(carResponse);

        }
        if (carRespons.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(carRespons);
        }
    }

    private CarResponse getCarResponse(Car car) {
        List<BookedCar> bookings = getAllBookingsByCarId(car.getId());
        List<BookingResponse> bookingInfo = bookings
                .stream()
                .map(booking -> new BookingResponse(booking.getBookingId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(), booking.getBookingConfirmationCode()))
                .toList();

        return new CarResponse(car.getId(),
                car.getCarType(), car.getCarPrice(),
                car.isBooked(), bookingInfo);
    }

    private List<BookedCar> getAllBookingsByCarId(Long carId) {
        return bookingServiceImpl.getAllBookingsByCarId(carId);

    }

}