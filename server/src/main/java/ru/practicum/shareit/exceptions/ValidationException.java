package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {
    public ValidationException(String message) {
        super(HttpStatus.NOT_ACCEPTABLE, message);
    }

}
