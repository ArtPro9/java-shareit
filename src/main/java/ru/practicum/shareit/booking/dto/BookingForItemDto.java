package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingForItemDto {
    private int id;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;

    public static BookingForItemDto consBookingForItemDto(Booking booking) {
        BookingForItemDto bookingForItemDto = new BookingForItemDto();
        bookingForItemDto.setId(booking.getId());
        bookingForItemDto.setBookerId(booking.getBooker().getId());
        bookingForItemDto.setStart(booking.getStartTime());
        bookingForItemDto.setEnd(booking.getEndTime());
        return bookingForItemDto;
    }
}
