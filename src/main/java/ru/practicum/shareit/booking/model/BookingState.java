package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum BookingState {
    ALL,
    PAST,
    CURRENT,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState fromString(Optional<String> state) {
        if (state.isEmpty()) {
            return BookingState.ALL;
        }
        try {
            return BookingState.valueOf(state.get());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown state: " + state.get());
        }
    }
}
