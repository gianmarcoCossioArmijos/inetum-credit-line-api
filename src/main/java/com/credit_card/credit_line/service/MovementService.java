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

        // credit line extrernal api validation available amount and update
        validateAvailableAmount(totalAmount, creditLineId);
        CredtLineApiResponse updatedAmount = updateavailableAmount(operationType, totalAmount, operationCurrency, creditLineId);

        // movement entity setup and persistenc
        LocalDateTime date = LocalDateTime.now();
        Movement movement = new Movement();
        movement.setMovementOperationAmount(request.operationAmount());
        movement.setMovementDate(date);
        movement.setMovementOperationtype(request.operationtype());
        movement.setMovementOperationtDescription(request.operationtDescription());
        movement.setMovementOperationCurrency(request.operationCurrency());
        movement.setMovementOrigin(request.origin());
        movement.setMovementChannel(request.channel());
        movement.setMovementCreditLineId(request.creditLineId());
        var savedMovement = repository.save(movement);
        var response = mapper.toResponse(savedMovement);

        // returning response
        Map<String, Object> responseObject = new HashMap<>();
        responseObject.put("responseMessage", "Movement succesfuly registered");
        responseObject.put("response", response);
        responseObject.put("responseAvailableAmount", updatedAmount);
        return responseObject;
    }

    public void validateAvailableAmount(Double totalAmount, int creditLineId){
        var apiResponse = apiService.creditLineAvailableAmount(creditLineId);
        if (apiResponse.getAvailable() < totalAmount) {
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
