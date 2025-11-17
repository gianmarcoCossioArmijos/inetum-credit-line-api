package com.credit_card.credit_line.dtos.response;

public record MovementResponse(

    int movementId,
    double operationAmount,
    double exchangedAmount,
    double exchangeRate,
    String createdAt,
    String operationtype,
    String operationtDescription,
    String operationCurrency,
    String exchangeCurrency,
    String origin,
    String channel,
    int creditLineId
) {}
