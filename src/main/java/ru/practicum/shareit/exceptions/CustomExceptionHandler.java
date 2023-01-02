package ru.practicum.shareit.exceptions;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({ValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ValidationException exception, WebRequest request) {
        log.error(exception.getMessage());
        ApiError apiError = new ApiError(exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({DuplicateEmailException.class})
    protected ResponseEntity<Object> handleConflictException(Exception exception, WebRequest request) {
        log.error(exception.getMessage());
        ApiError apiError = new ApiError(exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ItemAccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(Exception exception, WebRequest request) {
        log.error(exception.getMessage());
        ApiError apiError = new ApiError(exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(exception.getClass().getSimpleName(), "", errors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnknownIdException.class, SqlException.class, NoItemsFoundBySearchException.class})
    protected ResponseEntity<Object> handleNotFound(Exception exception, WebRequest request) {
        log.error(exception.getMessage());
        ApiError apiError = new ApiError(exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected @NonNull ResponseEntity<Object> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {

        ApiError apiError = new ApiError("Поля JSON заполнены некорректно", exception.getMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
        log.error(exception.getMessage());
        ApiError apiError = new ApiError(exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
