package ru.petrov.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.petrov.util.exception.ErrorResponse;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    private ResponseEntity<ErrorResponse> handleException(ConstraintViolationException e) {
        ErrorResponse response = new ErrorResponse(
                "Некорректные входные параметры. " +
                        e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}