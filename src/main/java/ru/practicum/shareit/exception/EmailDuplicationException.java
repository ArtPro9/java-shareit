package ru.practicum.shareit.exception;

public class EmailDuplicationException extends RuntimeException {
    public EmailDuplicationException(String message) {
        super(message);
    }
}
