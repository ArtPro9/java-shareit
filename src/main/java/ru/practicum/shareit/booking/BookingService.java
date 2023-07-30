package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking createBooking(Booking booking);

    Booking updateBookingStatusByOwner(Integer userId, Integer bookingId, Optional<Boolean> isApproved);

    Booking getBookingById(Integer userId, Integer bookingId);

    List<Booking> getBookingsWithState(Integer userId, BookingState state);

    List<Booking> getBookingsWithStateForOwner(Integer userId, BookingState state);
}
