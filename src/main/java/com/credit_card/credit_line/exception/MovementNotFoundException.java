package com.credit_card.credit_line.exception;

public class MovementNotFoundException extends RuntimeException {

    public MovementNotFoundException(String message) {
        super(message);
    }
}
