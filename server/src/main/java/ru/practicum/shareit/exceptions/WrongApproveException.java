package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WrongApproveException extends ResponseStatusException {
    public WrongApproveException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
