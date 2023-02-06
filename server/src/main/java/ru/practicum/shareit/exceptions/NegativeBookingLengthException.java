package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NegativeBookingLengthException extends ResponseStatusException {
    public NegativeBookingLengthException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
