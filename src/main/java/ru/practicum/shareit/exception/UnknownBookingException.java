package ru.practicum.shareit.exception;

public class UnknownBookingException extends RuntimeException {
    public UnknownBookingException(String message) {
        super(message);
    }

    public UnknownBookingException(Integer id) {
        super("Illegal booking id: " + id);
    }
}
