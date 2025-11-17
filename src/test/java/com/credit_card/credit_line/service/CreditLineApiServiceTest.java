package com.credit_card.credit_line.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.http.MediaType;

import com.credit_card.credit_line.api.CreditLineApiService;
import com.credit_card.credit_line.api.CredtLineApiResponse;
import com.credit_card.credit_line.api.UpdateAvailableAmountApiRequest;

@RestClientTest(CreditLineApiService.class)
public class CreditLineApiServiceTest {

    @Autowired
    private CreditLineApiService apiService;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void creditLineApiService_availableAmount_returnCreditLineApiResponse() throws Exception {
        CredtLineApiResponse apiResponse = new CredtLineApiResponse();
        apiResponse.setAvailableAmount(500.0);

        this.server.expect(ExpectedCount.once(),
            requestTo("http://localhost:8081/credit-card/api/v1/credit-line/available-amount/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                    """
                    { "availableAmount": 500.0 }
                    """, MediaType.APPLICATION_JSON));
        var result = apiService.creditLineAvailableAmount(1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getAvailableAmount()).isEqualTo(apiResponse.getAvailableAmount());
        this.server.verify();
    }

    @Test
    void creditLineApiService_availableAmount_CreditLineNotFoundException() throws Exception {
        this.server.expect(ExpectedCount.once(),
            requestTo("http://localhost:8081/credit-card/api/v1/credit-line/available-amount/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).body("Credit line not found"));

        Assertions.assertThatThrownBy(() -> apiService.creditLineAvailableAmount(1))
            .hasMessageContaining("Credit line not found");
    }

    @Test
    void creditLineApiService_updateAvailableAmount_returnCreditLineApiResponse() throws Exception {
        CredtLineApiResponse updateResponse = new CredtLineApiResponse();
        updateResponse.setCreditLineId(1);
        updateResponse.setCardNumber("0000-0000-0000-0001");
        updateResponse.setCreditLineCurrency("SOL");
        updateResponse.setCreditLine(1000.00);
        updateResponse.setAvailableAmount(800.00);
        updateResponse.setExchangedAmount(20.00);
        updateResponse.setExchangeRate(1.0);
        updateResponse.setCreditLineCurrency("SOL");

        this.server.expect(ExpectedCount.once(),
            requestTo("http://localhost:8081/credit-card/api/v1/credit-line/update-amount/1"))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withSuccess(
                    """
                    {
                        "creditLineId": 1,
                        "cardNumber": "0000-0000-0000-0001",
                        "creditLineCurrency": "SOL",
                        "creditLine": 1000.0,
                        "availableAmount": 800.0,
                        "exchangedAmount": 20.0,
                        "exchangeRate": 1.0
                    }
                    """, MediaType.APPLICATION_JSON));
        var result = apiService.updateavailableAmount("DEBIT", 20.00, "SOL", 1);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getCreditLineId()).isEqualTo(updateResponse.getCreditLineId());
        Assertions.assertThat(result.getCardNumber()).isEqualTo(updateResponse.getCardNumber());
        Assertions.assertThat(result.getCreditLineCurrency()).isEqualTo(updateResponse.getCreditLineCurrency());
        Assertions.assertThat(result.getCreditLine()).isEqualTo(updateResponse.getCreditLine());
        Assertions.assertThat(result.getAvailableAmount()).isEqualTo(updateResponse.getAvailableAmount());
        Assertions.assertThat(result.getExchangedAmount()).isEqualTo(updateResponse.getExchangedAmount());
        Assertions.assertThat(result.getExchangeRate()).isEqualTo(updateResponse.getExchangeRate());
        this.server.verify();
    }

    @Test
    void creditLineApiService_updateAvailableAmount_whenNull_throwsException() throws Exception {
        this.server.expect(ExpectedCount.once(),
            requestTo("http://localhost:8081/credit-card/api/v1/credit-line/update-amount/1"))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).body("Credit line not found"));
        UpdateAvailableAmountApiRequest request = new UpdateAvailableAmountApiRequest("DEBIT", 20.00, "SOL");
        Assertions.assertThatThrownBy(() -> apiService.updateavailableAmount(
                request.getOperationType(),
                request.getTotalAmount(),
                request.getOperationCurrency(),
                1))
            .hasMessageContaining("Credit line not found");
    }
}
