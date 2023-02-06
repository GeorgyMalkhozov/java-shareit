package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnknownIdException extends ResponseStatusException {
    public UnknownIdException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
