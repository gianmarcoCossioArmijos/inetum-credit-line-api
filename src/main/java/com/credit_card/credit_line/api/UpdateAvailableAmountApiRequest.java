package com.credit_card.credit_line.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateAvailableAmountApiRequest {

    private String operationType;
    private double totalAmount;
    private String operationCurrency;
}
