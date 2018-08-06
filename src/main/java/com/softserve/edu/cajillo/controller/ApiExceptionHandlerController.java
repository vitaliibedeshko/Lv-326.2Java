package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandlerController {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleExceptions(ServiceException e) {
        HttpStatus httpStatus;
        if (e instanceof DatabaseItemNotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (e instanceof ConflictException) {
            httpStatus = HttpStatus.CONFLICT;
        } else if (e instanceof UnsupportedMimeTypesException) {
            httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        } else if (e instanceof TokenExpiredException) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(e.getMessage());
    }
}