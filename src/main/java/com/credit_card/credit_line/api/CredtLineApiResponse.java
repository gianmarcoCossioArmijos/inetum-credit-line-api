package com.credit_card.credit_line.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredtLineApiResponse {

    private int creditLineId;
    private double availableAmount;
    private double exchangedAmount;
    private double exchangeRate;
    private double creditLine;
    private String cardNumber;
    private String creditLineCurrency;
}
