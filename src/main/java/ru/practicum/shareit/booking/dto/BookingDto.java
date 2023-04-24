package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    private int id;
    private Item item;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus bookingStatus;
}
