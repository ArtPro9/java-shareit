package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingOutputDto {
    private int id;
    private BookerDto booker;
    private BookingItemDto item;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
}
