package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper
public abstract class BookingMapper {
    public Booking enrichWithItemAndUser(Booking booking, Item item, User user) {
        booking.setItem(item);
        booking.setBooker(user);
        return booking;
    }

    @Mapping(source = "startTime", target = "start")
    @Mapping(source = "endTime", target = "end")
    @Mapping(source = "bookingStatus", target = "status")
    public abstract BookingOutputDto toDto(Booking booking);


    @Mapping(source = "start", target = "startTime")
    @Mapping(source = "end", target = "endTime")
    public abstract Booking toBooking(BookingInputDto bookingDto);
}
