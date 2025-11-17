package com.credit_card.credit_line.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movement")
public class Movement {

    @Id
    @Min(1)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "MOVEMENT_SEQUENCE", sequenceName = "MOVEMENT_SEQUENCE")
    private int movementIdCode;
    @Column(name = "operation_amount")
    private double movementOperationAmount;
    @Column(name = "exchanged_amount")
    private double movementExchangedAmount;
    @Column(name = "exchange_rate")
    private double movementExchangedRate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "creadtedAt")
    private LocalDateTime movementDate;
    @Column(name = "operation_type")
    private String  movementOperationtype;
    @Column(name = "operation_description")
    private String movementOperationtDescription;
    @Column(name = "operation_currency")
    private String movementOperationCurrency;
    @Column(name = "exchange_currency")
    private String movementExchangedCurrency;
    @Column(name = "origin")
    private String movementOrigin;
    @Column(name = "channel")
    private String movementChannel;
    @Column(name = "credit_line_id")
    private int movementCreditLineId;
}
