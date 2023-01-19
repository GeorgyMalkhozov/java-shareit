package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoObjectsFoundException extends ResponseStatusException {
    public NoObjectsFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
