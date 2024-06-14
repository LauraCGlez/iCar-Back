package com.icarapp.icar.controller;

import com.icarapp.icar.model.Car;
import com.icarapp.icar.model.CarType;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.icarapp.icar.exception.PhotoRetrievalException;
import com.icarapp.icar.exception.ResourceNotFoundException;
import com.icarapp.icar.model.BookedCar;
import com.icarapp.icar.response.BookingResponse;
import com.icarapp.icar.response.CarResponse;
import com.icarapp.icar.service.impl.BookingServiceImpl;
import com.icarapp.icar.service.CarService;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
        byte[] photoBytes = getDefaultImage(carType);
        Car savedCar = carService.addNewCar(Arrays.toString(photoBytes), carType, carPrice);
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
        List<CarResponse> carRespons = new ArrayList<>();
        for (Car car : cars) {
            byte[] photoBytes = carService.getCarPhotoByCarId(car.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                CarResponse carResponse = getCarResponse(car);
                carResponse.setPhoto(base64Photo);
                carRespons.add(carResponse);
            }
        }
        return ResponseEntity.ok(carRespons);
    }

    @DeleteMapping("/delete/car/{carId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCar(@PathVariable Long carId) {
        carService.deleteRoom(carId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{carId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long carId,
                                                 @RequestParam(required = false) CarType carType,
                                                 @RequestParam(required = false) BigDecimal roomPrice,
                                                 @RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {
        byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes()
                : carService.getCarPhotoByCarId(carId);
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Car theCar = carService.updateCar(carId, carType, roomPrice, photoBytes);
        theCar.setPhoto(photoBlob);
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
            byte[] photoBytes = carService.getCarPhotoByCarId(car.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                CarResponse carResponse = getCarResponse(car);
                carResponse.setPhoto(photoBase64);
                carRespons.add(carResponse);
            }
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
        byte[] photoBytes = null;
        Blob photoBlob = car.getPhoto();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new CarResponse(car.getId(),
                car.getCarType(), car.getCarPrice(),
                car.isBooked(), photoBytes, bookingInfo);
    }

    private List<BookedCar> getAllBookingsByCarId(Long roomId) {
        return bookingServiceImpl.getAllBookingsByCarId(roomId);

    }

    private byte[] getDefaultImage(CarType carType) throws IOException {
        String imageName = carType.name() + ".jpeg";
        Path file = Paths.get("src/main/resources/img/" + imageName);
        return Files.readAllBytes(file);
    }



}