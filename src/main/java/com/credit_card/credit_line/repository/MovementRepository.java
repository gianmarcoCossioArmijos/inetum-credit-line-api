package com.credit_card.credit_line.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.credit_card.credit_line.entity.Movement;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Integer> {

    @Query(value = "SELECT * FROM movement WHERE credit_line_id = :creditLineId AND operation_type = :movementOperationtype", nativeQuery = true)
    List<Movement> findByMovementCreditLineIdAndMovementOperationtype(@Param("creditLineId") int creditLineId,
                                                            @Param("movementOperationtype") String movementOperationtype);
}
