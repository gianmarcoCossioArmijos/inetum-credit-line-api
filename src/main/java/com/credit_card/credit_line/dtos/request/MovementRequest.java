package com.credit_card.credit_line.dtos.request;

import org.springframework.format.annotation.NumberFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovementRequest(

    @NotNull(message = "Operation amount should not be null")
    @Positive(message = "Operation amount should be a positive number")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @DecimalMin(value = "0.0", message = "Operation amount minimum value should be 0.0")
    @Digits(integer = 5, fraction = 2, message = "Value can have at most 5 integer digits and 2 fractional digits.")
    double operationAmount,
    @NotNull(message = "Operation type should not be null")
    @NotBlank(message = "Operation type should not be blank")
    String operationtype,
    @NotNull(message = "Operation description should not be null")
    @NotBlank(message = "Operation description should not be blank")
    String operationtDescription,
    @NotNull(message = "Operation currency should not be null")
    @NotBlank(message = "Operation currency should not be blank")
    String operationCurrency,
    @NotNull(message = "Operation origin entity should not be null")
    @NotBlank(message = "Operation origin entity should not be blank")
    String origin,
    @NotNull(message = "Operation channel should not be null")
    @NotBlank(message = "Operation channel should not be blank")
    String channel,
    @NotNull(message = "Credit line ID should not be null")
    @Positive(message = "Operation amount should be a positive number")
    int creditLineId
){}