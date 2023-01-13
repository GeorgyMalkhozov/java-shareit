package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ObjectAccessDeniedException extends ResponseStatusException {
    public ObjectAccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

}
