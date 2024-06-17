package com.icarapp.icar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icarapp.icar.model.BookedCar;

import java.util.List;
import java.util.Optional;



public interface BookingRepository extends JpaRepository<BookedCar, Long> {

    List<BookedCar> findByCarId(Long roomId);

    Optional<BookedCar> findByBookingConfirmationCode(String confirmationCode);

    List<BookedCar> findByGuestEmail(String email);
}
