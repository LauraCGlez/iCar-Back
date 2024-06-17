package com.icarapp.icar.response;

import com.icarapp.icar.model.CarType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
public class CarResponse {
    private Long id;
    private CarType carType;
    private BigDecimal carPrice;
    private boolean isBooked;
    private List<BookingResponse>bookings;

    public CarResponse(Long id, CarType carType, BigDecimal carPrice) {
        this.id = id;
        this.carType = carType;
        this.carPrice = carPrice;
    }

    public CarResponse(Long id, CarType carType, BigDecimal carPrice, boolean isBooked, List<BookingResponse> bookings) {
        this.id = id;
        this.carType = carType;
        this.carPrice = carPrice;
        this.isBooked = isBooked;

        this.bookings = bookings;
    }

}
