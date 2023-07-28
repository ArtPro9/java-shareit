package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingForItemDto {
    private int id;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;

    public static BookingForItemDto consBookingForItemDto(Booking booking) {
        return builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStartTime())
                .end(booking.getEndTime())
                .build();
    }
}
