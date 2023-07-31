package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper
public abstract class BookingMapper {
    public static BookingForItemDto consBookingForItemDto(Booking booking) {
        BookingForItemDto bookingForItemDto = new BookingForItemDto();
        bookingForItemDto.setId(booking.getId());
        bookingForItemDto.setBookerId(booking.getBooker().getId());
        bookingForItemDto.setStart(booking.getStartTime());
        bookingForItemDto.setEnd(booking.getEndTime());
        return bookingForItemDto;
    }

    @Mapping(source = "startTime", target = "start")
    @Mapping(source = "endTime", target = "end")
    @Mapping(source = "bookingStatus", target = "status")
    public abstract BookingOutputDto toDto(Booking booking);

    @Mapping(source = "start", target = "startTime")
    @Mapping(source = "end", target = "endTime")
    public abstract Booking toBooking(BookingInputDto bookingDto);

    public Booking toBooking(BookingInputDto bookingDto, Item item, User user) {
        return enrichWithItemAndUser(toBooking(bookingDto), item, user);
    }

    private Booking enrichWithItemAndUser(Booking booking, Item item, User user) {
        booking.setItem(item);
        booking.setBooker(user);
        return booking;
    }
}
