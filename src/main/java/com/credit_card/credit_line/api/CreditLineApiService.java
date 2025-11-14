package com.credit_card.credit_line.api;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.credit_card.credit_line.exception.CreditLineNotFoundException;

@Service
public class CreditLineApiService {

    private final RestClient restClient;

    public CreditLineApiService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
            .baseUrl("http://localhost:8081/credit-card/api/v1/credit-line")
            .defaultHeader("Accept", "application/json")
            .build();
    }

    public CredtLineApiResponse creditLineAvailableAmount(int creditLineId) {
        CredtLineApiResponse response = restClient.get()
            .uri("/available-amount/{id}", creditLineId)
            .retrieve()
            .body(CredtLineApiResponse.class);

        if (response == null) {
            throw new CreditLineNotFoundException("Credit line service returned empty response, maybe entity was not found");
        }
        return response;
    }

    public CredtLineApiResponse updateavailableAmount(String operationType, Double totalAmount, String operationCurrency, int creditLineId) {
        UpdateAvailableAmountApiRequest request = new UpdateAvailableAmountApiRequest(operationType, totalAmount, operationCurrency);
        CredtLineApiResponse response = restClient.put()
            .uri("/update-amount/{id}", creditLineId)
            .contentType(MediaType.APPLICATION_JSON) 
            .body(request)
            .retrieve()
            .body(CredtLineApiResponse.class);
        if (response == null) {
            throw new CreditLineNotFoundException("Credit line service returned empty response, maybe entity was not found");
        }
        return response;
    }
}
