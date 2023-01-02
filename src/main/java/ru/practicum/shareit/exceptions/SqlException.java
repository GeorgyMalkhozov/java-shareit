package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SqlException extends ResponseStatusException {
    public SqlException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
