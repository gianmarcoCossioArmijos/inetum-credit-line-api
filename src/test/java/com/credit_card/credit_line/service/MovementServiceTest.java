package com.credit_card.credit_line.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.credit_card.credit_line.api.CreditLineApiService;
import com.credit_card.credit_line.api.CredtLineApiResponse;
import com.credit_card.credit_line.dtos.mapper.MovementMapper;
import com.credit_card.credit_line.dtos.request.MovementRequest;
import com.credit_card.credit_line.dtos.response.MovementResponse;
import com.credit_card.credit_line.entity.Movement;
import com.credit_card.credit_line.exception.CreditLineInsufficientFundsException;
import com.credit_card.credit_line.repository.MovementRepository;

@ExtendWith(MockitoExtension.class)
public class MovementServiceTest {

    @Mock
    private MovementRepository repository;

    @Mock
    private MovementMapper mapper;

    @Mock
    private CreditLineApiService apiService;

    @InjectMocks
    private MovementService service;

    @Test
    void movementService_createMovement_returnRegisteredMovement() throws Exception {
        MovementRequest request = new MovementRequest(
                20.00,
                "DEBIT",
                "purchase payment",
                "DOLLAR",
                "FALABELLA RETAIL S.A.C.",
                "ECOMMERCE",
                1);

        // mock validateAvailableAmount
        CredtLineApiResponse validateResponse = new CredtLineApiResponse();
        validateResponse.setAvailableAmount(1000.0);
        Mockito.when(apiService.creditLineAvailableAmount(1)).thenReturn(validateResponse);

        // mock updateavailableAmount
        CredtLineApiResponse updateResponse = new CredtLineApiResponse();
        updateResponse.setCreditLine(1);
        updateResponse.setAvailableAmount(800.00);
        updateResponse.setExchangedAmount(20.00);
        updateResponse.setExchangeRate(1.0);
        updateResponse.setCreditLineCurrency("SOL");
        Mockito.when(apiService.updateavailableAmount(
            "DEBIT",
            20.00,
            "DOLLAR",
            1)).thenReturn(updateResponse);

        // save movement mock
        Movement savedMovement = new Movement();
        savedMovement.setMovementIdCode(1);
        savedMovement.setMovementOperationAmount(20.00);
        savedMovement.setMovementExchangedAmount(2.00);
        savedMovement.setMovementExchangedRate(1.0);
        savedMovement.setMovementOperationtype("DEBIT");
        savedMovement.setMovementOperationtDescription("purchase payment");
        savedMovement.setMovementOperationCurrency("DOLLAR");
        savedMovement.setMovementExchangedCurrency("SOL");
        savedMovement.setMovementOrigin("FALABELLA RETAIL S.A.C.");
        savedMovement.setMovementChannel("ECOMMERCE");
        savedMovement.setMovementCreditLineId(1);
        savedMovement.setMovementDate(LocalDateTime.now());
        Mockito.when(repository.save(Mockito.any(Movement.class))).thenReturn(savedMovement);

        // mock mapper
        MovementResponse movementResponse = new MovementResponse(
                1,
                20.00,
                20.00,
                1.0,
                LocalDateTime.now().toString(),
                "DEBIT",
                "purchase payment",
                "DOLLAR",
                "SOL",
                "FALABELLA RETAIL S.A.C.",
                "ECOMMERCE",
                1);
                
        Mockito.when(mapper.toResponse(savedMovement)).thenReturn(movementResponse);
        Map<String, Object> result = service.createMovement(request);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).containsKeys("responseMessage", "responseCreditAmount", "responseCurrentAmount", "response");
        Assertions.assertThat(result.get("responseMessage")).isEqualTo("Movement succesfuly registered");
        Assertions.assertThat(result.get("responseCreditAmount")).isEqualTo(updateResponse.getCreditLine());
        Assertions.assertThat(result.get("responseCurrentAmount")).isEqualTo(updateResponse.getAvailableAmount());
        Assertions.assertThat(result.get("response")).isEqualTo(movementResponse);

        Mockito.verify(apiService).creditLineAvailableAmount(1);
        Mockito.verify(apiService).updateavailableAmount("DEBIT", 20.00, "DOLLAR", 1);
        Mockito.verify(repository).save(Mockito.any(Movement.class));
        Mockito.verify(mapper).toResponse(savedMovement);
        Mockito.verifyNoMoreInteractions(apiService, repository, mapper);
    }

    @Test
    void movementService_validateAvailableAmount() throws Exception {
        CredtLineApiResponse validateResponse = new CredtLineApiResponse();
        validateResponse.setCreditLineId(1);
        validateResponse.setCreditLine(1000.00);
        validateResponse.setCardNumber("0000-0000-0000-0001");
        validateResponse.setCreditLineCurrency("SOL");
        validateResponse.setAvailableAmount(800.00);

        Mockito.when(apiService.creditLineAvailableAmount(1)).thenReturn(validateResponse);
        CredtLineApiResponse result = apiService.creditLineAvailableAmount(1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getCreditLineId()).isEqualTo(validateResponse.getCreditLineId());
        Assertions.assertThat(result.getCreditLine()).isEqualTo(validateResponse.getCreditLine());
        Assertions.assertThat(result.getCardNumber()).isEqualTo(validateResponse.getCardNumber());
        Assertions.assertThat(result.getCreditLineCurrency()).isEqualTo(validateResponse.getCreditLineCurrency());
        Assertions.assertThat(result.getAvailableAmount()).isEqualTo(validateResponse.getAvailableAmount());

        Mockito.verify(apiService).creditLineAvailableAmount(1);
        Mockito.verifyNoMoreInteractions(apiService);
    }

    @Test
    void movementService_validateAvailableAmount_creditLineInsufficientFundsException() throws Exception {
        CredtLineApiResponse response = new CredtLineApiResponse();
        response.setAvailableAmount(800.00);

        Mockito.when(apiService.creditLineAvailableAmount(1)).thenReturn(response);
        Assertions.assertThatThrownBy(() -> service.validateAvailableAmount(900.00, 1))
            .isInstanceOf(CreditLineInsufficientFundsException.class)
            .hasMessage("Insufficient funds for operation");
    }

    @Test
    void movementService_updateavailableAmount_returnCredtLineApiResponse() throws Exception {
        // mock updateavailableAmount
        CredtLineApiResponse updateResponse = new CredtLineApiResponse();
        updateResponse.setCreditLineId(1);
        updateResponse.setCreditLine(1000.00);
        updateResponse.setCardNumber("0000-0000-0000-0001");
        updateResponse.setCreditLineCurrency("SOL");
        updateResponse.setAvailableAmount(800.00);
        updateResponse.setExchangedAmount(20.00);
        updateResponse.setExchangeRate(1.0);

        Mockito.when(apiService.updateavailableAmount(
            "DEBIT",
            20.00,
            "SOL",
            1)).thenReturn(updateResponse);
        CredtLineApiResponse result = apiService.updateavailableAmount("DEBIT", 20.00, "SOL", 1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getCreditLineId()).isEqualTo(updateResponse.getCreditLineId());
        Assertions.assertThat(result.getCreditLine()).isEqualTo(updateResponse.getCreditLine());
        Assertions.assertThat(result.getCardNumber()).isEqualTo(updateResponse.getCardNumber());
        Assertions.assertThat(result.getCreditLineCurrency()).isEqualTo(updateResponse.getCreditLineCurrency());
        Assertions.assertThat(result.getAvailableAmount()).isEqualTo(updateResponse.getAvailableAmount());
        Assertions.assertThat(result.getExchangedAmount()).isEqualTo(updateResponse.getExchangedAmount());
        Assertions.assertThat(result.getExchangeRate()).isEqualTo(updateResponse.getExchangeRate());

        Mockito.verify(apiService).updateavailableAmount("DEBIT", 20.00, "SOL", 1);
        Mockito.verifyNoMoreInteractions(apiService);
    }

    @Test
    void movementService_getMovementsByCreditLineIdAndType_returnMovementsList() throws Exception {
        Movement movement =  Movement.builder()
            .movementIdCode(1)
            .movementOperationAmount(290.90)
            .movementExchangedAmount(1200.00)
            .movementExchangedRate(3.20)
            .movementDate(LocalDateTime.now())
            .movementOperationtype("DEBIT")
            .movementOperationtDescription("purchase payment")
            .movementOperationCurrency("DOLLAR")
            .movementExchangedCurrency("SOL")
            .movementOrigin("FALABELLA RETAIL S.A.C.")
            .movementChannel("ECOMMERCE")
            .movementCreditLineId(1)
            .build();

        MovementResponse response = new MovementResponse(
            1,
            290.90,
            1200.00,
            3.20,
            LocalDateTime.now().toString(),
            "DEBIT",
            "purchase payment",
            "DOLLAR",
            "SOL",
            "FALABELLA RETAIL S.A.C.",
            "ECOMMERCE",
            1);

        Mockito.when(repository.findByMovementCreditLineIdAndMovementOperationtype(Mockito.anyInt(),Mockito.anyString())).thenReturn(List.of(movement));
        Mockito.when(mapper.toResponse(movement)).thenReturn(response);
        List<MovementResponse> movements = service.getMovementsByType(1, "DEBIT");

        Assertions.assertThat(movements).isNotNull();
        Assertions.assertThat(movements.get(0).movementId()).isEqualTo(movement.getMovementIdCode());
        Assertions.assertThat(movements.get(0).operationAmount()).isEqualTo(movement.getMovementOperationAmount());
        Assertions.assertThat(movements.get(0).exchangedAmount()).isEqualTo(movement.getMovementExchangedAmount());
        Assertions.assertThat(movements.get(0).exchangeRate()).isEqualTo(movement.getMovementExchangedRate());
        Assertions.assertThat(movements.get(0).createdAt()).isEqualTo(movement.getMovementDate().toString());
        Assertions.assertThat(movements.get(0).operationtype()).isEqualTo(movement.getMovementOperationtype());
        Assertions.assertThat(movements.get(0).operationtDescription()).isEqualTo(movement.getMovementOperationtDescription());
        Assertions.assertThat(movements.get(0).operationCurrency()).isEqualTo(movement.getMovementOperationCurrency());
        Assertions.assertThat(movements.get(0).exchangeCurrency()).isEqualTo(movement.getMovementExchangedCurrency());
        Assertions.assertThat(movements.get(0).origin()).isEqualTo(movement.getMovementOrigin());
        Assertions.assertThat(movements.get(0).channel()).isEqualTo(movement.getMovementChannel());
        Assertions.assertThat(movements.get(0).creditLineId()).isEqualTo(movement.getMovementCreditLineId());
    
        Mockito.verify(repository).findByMovementCreditLineIdAndMovementOperationtype(1, "DEBIT");
        Mockito.verify(mapper).toResponse(movement);
        Mockito.verifyNoMoreInteractions(repository,mapper);
    }
}
