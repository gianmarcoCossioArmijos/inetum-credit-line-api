package com.credit_card.credit_line.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.credit_card.credit_line.dtos.request.MovementRequest;
import com.credit_card.credit_line.dtos.response.MovementResponse;
import com.credit_card.credit_line.service.MovementService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movement")
public class MovementController {

    private final MovementService service;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createMovement(@Valid @RequestBody MovementRequest request) {
        return ResponseEntity.ok(service.createMovement(request));
    }

    @GetMapping("/type-report/{creditLineId}")
    public ResponseEntity<List<MovementResponse>> getMovementsByType(@PathVariable("creditLineId") int creditLineId,
                                                                    @RequestBody MovementRequest request) {
        return ResponseEntity.ok(service.getMovementsByType(creditLineId, request.operationtype()));
    }
}
