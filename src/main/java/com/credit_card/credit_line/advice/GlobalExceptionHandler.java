package com.credit_card.credit_line.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.credit_card.credit_line.exception.CreditLineNotFoundException;
import com.credit_card.credit_line.exception.DateParseException;
import com.credit_card.credit_line.exception.MovementNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovementNotFoundException.class)
    public ResponseEntity<Map<String, Object>> creditLineNotFound(MovementNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity <Map<String, Object>>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CreditLineNotFoundException.class)
    public ResponseEntity<Map<String, Object>> creditLineNotFound(CreditLineNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity <Map<String, Object>>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DateParseException.class)
    public ResponseEntity<Map<String, Object>> creditLineNotFound(DateParseException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity <Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validationHandler(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
            .getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> response = new HashMap<>();
        response.put("error", errors);
        return new ResponseEntity <Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> generalHandler(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "An unexpected error occurred");
        response.put("errorMessage", ex.getMessage());
        response.put("errorCause", ex.getCause());
        return new ResponseEntity <Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
