package com.icarapp.icar.service;

import java.util.List;

import com.icarapp.icar.model.BookedCar;


public interface BookingService {
    void cancelBooking(Long bookingId);

    List<BookedCar> getAllBookingsByCarId(Long roomId);

    String saveBooking(Long roomId, BookedCar bookingRequest);

    BookedCar findByBookingConfirmationCode(String confirmationCode);

    List<BookedCar> getAllBookings();

    List<BookedCar> getBookingsByUserEmail(String email);
}
