package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoItemsFoundBySearchException extends ResponseStatusException {
    public NoItemsFoundBySearchException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
