package ru.practicum.shareit.booking.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private int id;
    private int itemId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus bookingStatus;
}
