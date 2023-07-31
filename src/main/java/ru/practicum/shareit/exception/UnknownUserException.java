package ru.practicum.shareit.exception;

public class UnknownUserException extends RuntimeException {
    public UnknownUserException(String message) {
        super(message);
    }

    public UnknownUserException(Integer id) {
        super("Illegal user id: " + id);
    }
}
