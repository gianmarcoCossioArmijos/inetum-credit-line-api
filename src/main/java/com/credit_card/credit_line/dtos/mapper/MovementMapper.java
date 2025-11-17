package com.credit_card.credit_line.dtos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.credit_card.credit_line.dtos.request.MovementRequest;
import com.credit_card.credit_line.dtos.response.MovementResponse;
import com.credit_card.credit_line.entity.Movement;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MovementMapper {

    @Mapping(target = "movementIdCode", ignore = true)
    @Mapping(source = "operationAmount", target = "movementOperationAmount")
    @Mapping(target = "movementExchangedAmount", ignore = true)
    @Mapping(target = "movementExchangedRate", ignore = true)
    @Mapping(target = "movementDate", ignore = true)
    @Mapping(source = "operationtype", target = "movementOperationtype")
    @Mapping(source = "operationtDescription", target = "movementOperationtDescription")
    @Mapping(source = "operationCurrency", target = "movementOperationCurrency")
    @Mapping(target = "movementExchangedCurrency", ignore = true)
    @Mapping(source = "origin", target = "movementOrigin")
    @Mapping(source = "channel", target = "movementChannel")
    @Mapping(source = "creditLineId", target = "movementCreditLineId")
    Movement toEntity(MovementRequest request);

    @Mapping(source = "movementIdCode", target = "movementId")
    @Mapping(source = "movementOperationAmount", target = "operationAmount")
    @Mapping(source = "movementExchangedAmount", target = "exchangedAmount")
    @Mapping(source = "movementExchangedRate", target = "exchangeRate")
    @Mapping(source = "movementDate", target = "createdAt")
    @Mapping(source = "movementOperationtype", target = "operationtype")
    @Mapping(source = "movementOperationtDescription", target = "operationtDescription")
    @Mapping(source = "movementOperationCurrency", target = "operationCurrency")
    @Mapping(source = "movementExchangedCurrency", target = "exchangeCurrency")
    @Mapping(source = "movementOrigin", target = "origin")
    @Mapping(source = "movementChannel", target = "channel")
    @Mapping(source = "movementCreditLineId", target = "creditLineId")
    MovementResponse toResponse(Movement movement);
}
