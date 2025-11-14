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
public class CredtLineApiResponse {

    private int creditLineId;
    private double available;
    private double credit;
    private String cardNumber;
    private String creditLineCurrency;
}
