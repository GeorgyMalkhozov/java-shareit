package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoCommentWithoutBookingException extends ResponseStatusException {
    public NoCommentWithoutBookingException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
