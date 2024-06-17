package com.icarapp.icar.service.impl;


import com.icarapp.icar.model.Car;
import com.icarapp.icar.service.BookingService;
import com.icarapp.icar.service.CarService;
import com.icarapp.icar.exception.InvalidBookingRequestException;
import com.icarapp.icar.exception.ResourceNotFoundException;
import com.icarapp.icar.model.BookedCar;
import com.icarapp.icar.repository.BookingRepository;
import com.icarapp.icar.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final CarService carService;
    private final EmailService emailService;


    @Override
    public List<BookedCar> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookedCar> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public List<BookedCar> getAllBookingsByCarId(Long carId) {
        return bookingRepository.findByCarId(carId);
    }

    @Override
    public String saveBooking(Long carId, BookedCar bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }
        Car car = carService.getCarById(carId).get();
        List<BookedCar> existingBookings = car.getBookings();
        boolean carIsAvailable = carIsAvailable(bookingRequest,existingBookings);
        if (carIsAvailable){
            car.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);

            String subject = "Car Booking Confirmation";
            String messageContent = "Dear " + bookingRequest.getGuestFullName() + ",\n\n" +
                    "Your booking for a car type " + bookingRequest.getCar().getCarType() + " has been confirmed.\n\n" +
                    "Your confirmation code is: " + bookingRequest.getBookingConfirmationCode() + "\n\n" +
                    "Thank you for choosing our service!";
            emailService.sendConfirmationEmail(bookingRequest.getGuestEmail(), subject, messageContent);

        }else{
            throw  new InvalidBookingRequestException("Sorry, This car is not available for the selected dates;");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public BookedCar findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code :"+confirmationCode));

    }


    private boolean carIsAvailable(BookedCar bookingRequest, List<BookedCar> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

}
