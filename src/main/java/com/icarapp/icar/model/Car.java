package com.icarapp.icar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Enumerated(EnumType.STRING)
    private CarType carType;
    private BigDecimal carPrice;
    private boolean isBooked = false;

    @OneToMany(mappedBy="car", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedCar> bookings;

    public Car() {
        this.bookings = new ArrayList<>();
    }
    public void addBooking(BookedCar booking){
        if (bookings == null){
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setCar(this);
        isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10);
        booking.setBookingConfirmationCode(bookingCode);
    }
}
