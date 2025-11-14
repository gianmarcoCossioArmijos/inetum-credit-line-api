package com.credit_card.credit_line.exception;

public class CreditLineInsufficientFundsException extends RuntimeException {

    public CreditLineInsufficientFundsException(String message) {
        super(message);
    }
}
