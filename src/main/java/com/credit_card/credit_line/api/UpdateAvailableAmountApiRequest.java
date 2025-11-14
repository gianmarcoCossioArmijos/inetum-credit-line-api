package com.credit_card.credit_line.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAvailableAmountApiRequest {

    private String operationType;
    private double totalAmount;
    private String operationCurrency;
}
