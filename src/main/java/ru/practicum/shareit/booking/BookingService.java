package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking);

    Booking updateBookingStatusByOwner(User userId, Integer bookingId, Boolean isApproved);

    Booking getBookingById(User userId, Integer bookingId);

    List<Booking> getBookingsWithState(User userId, BookingState state);

    List<Booking> getBookingsWithStateForOwner(User userId, BookingState state);
}
