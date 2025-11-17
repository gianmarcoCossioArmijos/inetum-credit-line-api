package com.credit_card.credit_line.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.credit_card.credit_line.api.CreditLineApiService;
import com.credit_card.credit_line.api.CredtLineApiResponse;
import com.credit_card.credit_line.dtos.mapper.MovementMapper;
import com.credit_card.credit_line.dtos.request.MovementRequest;
import com.credit_card.credit_line.dtos.response.MovementResponse;
import com.credit_card.credit_line.entity.Movement;
import com.credit_card.credit_line.exception.CreditLineInsufficientFundsException;
import com.credit_card.credit_line.repository.MovementRepository;

@Service
@RequiredArgsConstructor
public class MovementService {

    private final MovementMapper mapper;
    private final MovementRepository repository;
    private final CreditLineApiService apiService;

    public Map<String, Object> createMovement(MovementRequest request) {
        // validate available amount
        final int creditLineId = request.creditLineId();
        final Double totalAmount = request.operationAmount();
        final String operationCurrency = request.operationCurrency();
        final String operationType = request.operationtype();
        final String operationDescription = request.operationtDescription();
        LocalDateTime date = LocalDateTime.now();

        // credit line extrernal api validation available amount and update
        validateAvailableAmount(totalAmount, creditLineId);
        CredtLineApiResponse updatedAmount = updateavailableAmount(operationType, totalAmount, operationCurrency, creditLineId);

        // movement entity setup and persistence
        Movement movement = Movement.builder()
            .movementOperationAmount(totalAmount)
            .movementExchangedAmount(updatedAmount.getExchangedAmount())
            .movementExchangedRate(updatedAmount.getExchangeRate())
            .movementDate(date)
            .movementOperationtype(operationType)
            .movementOperationtDescription(operationDescription)
            .movementOperationCurrency(operationCurrency)
            .movementExchangedCurrency(updatedAmount.getCreditLineCurrency())
            .movementOrigin(request.origin())
            .movementChannel(request.channel())
            .movementCreditLineId(creditLineId)
            .build();
        var savedMovement = repository.save(movement);
        var response = mapper.toResponse(savedMovement);

        // returning response
        Map<String, Object> responseObject = new HashMap<>();
        responseObject.put("responseMessage", "Movement succesfuly registered");
        responseObject.put("responseCreditAmount", updatedAmount.getCreditLine());
        responseObject.put("responseCurrentAmount", updatedAmount.getAvailableAmount());
        responseObject.put("response", response);
        return responseObject;
    }

    public void validateAvailableAmount(Double totalAmount, int creditLineId){
        var apiResponse = apiService.creditLineAvailableAmount(creditLineId);
        if (apiResponse.getAvailableAmount() < totalAmount) {
            throw new CreditLineInsufficientFundsException("Insufficient funds for operation");
        }
    }

    public CredtLineApiResponse updateavailableAmount(String operationType, Double totalAmount, String operationCurrency, int creditLineId){
        var apiResponse = apiService.updateavailableAmount(operationType, totalAmount, operationCurrency, creditLineId);
        return apiResponse;
    }

    public List<MovementResponse> getMovementsByType(int creditLineId, String operationtype) {
        var response = repository.findByMovementCreditLineIdAndMovementOperationtype(creditLineId, operationtype);
        List<MovementResponse> movements = response
            .stream()
            .map(movement -> mapper.toResponse(movement))
            .toList();
        return movements;
    }
}
