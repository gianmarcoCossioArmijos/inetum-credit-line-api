package com.credit_card.credit_line.dtos.response;

public record MovementResponse(

    int movementId,
    double operationAmount,
    String createdAt,
    String operationtype,
    String operationtDescription,
    String operationCurrency,
    String origin,
    String channel,
    int creditLineId
) {}
