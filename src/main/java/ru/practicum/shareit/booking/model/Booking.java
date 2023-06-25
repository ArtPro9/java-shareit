package ru.practicum.shareit.booking.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int itemId;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Enumerated
    private BookingStatus bookingStatus;
}
