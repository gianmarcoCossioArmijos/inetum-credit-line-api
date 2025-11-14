package com.credit_card.credit_line.exception;

public class CreditLineNotFoundException extends RuntimeException {

    public CreditLineNotFoundException(String message) {
        super(message);
    }
}
