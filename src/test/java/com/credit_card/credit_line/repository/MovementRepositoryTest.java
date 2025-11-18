package com.credit_card.credit_line.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import com.credit_card.credit_line.entity.Movement;

@ExtendWith(MockitoExtension.class)
public class MovementRepositoryTest {

    @Mock
    private MovementRepository repository;

    @Test
    public void movementRepository_createMovement_returnRegisteredMovement() {
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
        
        Mockito.when(repository.save(Mockito.any(Movement.class))).thenReturn(movement);
        Movement registered = repository.save(movement);
        Assertions.assertThat(registered).isNotNull();
        Assertions.assertThat(registered.getMovementIdCode()).isGreaterThan(0);
        Assertions.assertThat(registered.getMovementOperationAmount()).isGreaterThan(0);
        Assertions.assertThat(registered.getMovementExchangedAmount()).isGreaterThan(0);
        Assertions.assertThat(registered.getMovementExchangedRate()).isGreaterThan(0);
        Assertions.assertThat(registered.getMovementDate()).isNotNull();
        Assertions.assertThat(registered.getMovementOperationtDescription()).isNotNull();
        Assertions.assertThat(registered.getMovementOperationCurrency()).isNotNull();
        Assertions.assertThat(registered.getMovementExchangedCurrency()).isNotNull();
        Assertions.assertThat(registered.getMovementOrigin()).isNotNull();
        Assertions.assertThat(registered.getMovementChannel()).isNotNull();
        Assertions.assertThat(registered.getMovementCreditLineId()).isGreaterThan(0);
    }

    @Test
    public void movementRepository_findByMovementCreditLineIdAndMovementOperationtype_returnFoundMovements() {
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

        List<Movement> reponselist = new ArrayList<>();
        List<Movement> found = new ArrayList<>();
        reponselist.add(movement);

        Mockito.when(repository.findByMovementCreditLineIdAndMovementOperationtype(1, "DEBIT"))
            .thenReturn(reponselist);
        found = repository.findByMovementCreditLineIdAndMovementOperationtype( 1, "DEBIT");
        Assertions.assertThat(found).isNotNull();
    }
}
