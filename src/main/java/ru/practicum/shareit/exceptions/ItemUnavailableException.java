package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ItemUnavailableException extends ResponseStatusException {
    public ItemUnavailableException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
