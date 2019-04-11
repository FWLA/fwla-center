package de.ihrigb.fwla.fwlacenter.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.ihrigb.fwla.fwlacenter.web.model.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse<?>> handleException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(e.getMessage(), e.getEntity()));
    }
}
