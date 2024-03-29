package ru.practicum.shareit.exception;

public class UnknownItemException extends RuntimeException {
    public UnknownItemException(String message) {
        super(message);
    }

    public UnknownItemException(Integer id) {
        super("Illegal item id: " + id);
    }
}
