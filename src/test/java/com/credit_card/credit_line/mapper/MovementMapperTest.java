package com.credit_card.credit_line.mapper;

import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.credit_card.credit_line.dtos.mapper.MovementMapper;
import com.credit_card.credit_line.dtos.request.MovementRequest;
import com.credit_card.credit_line.dtos.response.MovementResponse;
import com.credit_card.credit_line.entity.Movement;

@SpringBootTest
public class MovementMapperTest {

    @Autowired
    private MovementMapper mapper;

    @Test
	void movementMapper_toEntity() {
		MovementRequest request = new MovementRequest(
            20.00,
            "DEBIT",
            "payment",
            "SOL",
            "FALABELLA RETAIL S.A.C.",
            "ECOMMERCE",
            1);
        LocalDateTime date = LocalDateTime.now();
        String exchangeCurrency = "SOL";
        Double exchangedAmount = 20.00;
        Double exchangeRate = 1.00;

		Movement entity = mapper.toEntity(request);
        entity.setMovementDate(date);
        entity.setMovementExchangedCurrency(exchangeCurrency);
        entity.setMovementExchangedAmount(exchangedAmount);
        entity.setMovementExchangedRate(exchangeRate);

        Assertions.assertThat(entity).isNotNull();
		Assertions.assertThat(entity.getMovementOperationAmount()).isEqualTo(request.operationAmount());
        Assertions.assertThat(entity.getMovementExchangedAmount()).isEqualTo(exchangedAmount);
        Assertions.assertThat(entity.getMovementExchangedRate()).isEqualTo(exchangeRate);
		Assertions.assertThat(entity.getMovementOperationtype()).isEqualTo(request.operationtype());
        Assertions.assertThat(entity.getMovementDate()).isEqualTo(date);
		Assertions.assertThat(entity.getMovementOperationtDescription()).isEqualTo(request.operationtDescription());
		Assertions.assertThat(entity.getMovementOperationCurrency()).isEqualTo(request.operationCurrency());
        Assertions.assertThat(entity.getMovementExchangedCurrency()).isEqualTo(exchangeCurrency);
		Assertions.assertThat(entity.getMovementOrigin()).isEqualTo(request.origin());
        Assertions.assertThat(entity.getMovementChannel()).isEqualTo(request.channel());
        Assertions.assertThat(entity.getMovementCreditLineId()).isEqualTo(request.creditLineId());
	}

	@Test
	void movementMapperTest_toResponse() {
		Movement entity = Movement.builder()
            .movementIdCode(1)
            .movementOperationAmount(20.00)
            .movementOperationtype("DEBIT")
            .movementOperationtDescription("payment")
            .movementOperationCurrency("SOL")
            .movementOrigin("FALABELLA RETAIL S.A.C.")
            .movementChannel("ECOMMERCE")
            .movementCreditLineId(1)
            .build();

		MovementResponse response = mapper.toResponse(entity);

        Assertions.assertThat(entity).isNotNull();
		Assertions.assertThat(entity.getMovementIdCode()).isEqualTo(response.movementId());
		Assertions.assertThat(entity.getMovementOperationAmount()).isEqualTo(response.operationAmount());
		Assertions.assertThat(entity.getMovementOperationtype()).isEqualTo(response.operationtype());
		Assertions.assertThat(entity.getMovementOperationtDescription()).isEqualTo(response.operationtDescription());
		Assertions.assertThat(entity.getMovementOperationCurrency()).isEqualTo(response.operationCurrency());
		Assertions.assertThat(entity.getMovementOrigin()).isEqualTo(response.origin());
        Assertions.assertThat(entity.getMovementChannel()).isEqualTo(response.channel());
        Assertions.assertThat(entity.getMovementCreditLineId()).isEqualTo(response.creditLineId());
	}
}
