package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ItemAccessDeniedException extends ResponseStatusException {
    public ItemAccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

}
