package com.credit_card.credit_line.controller;

import static org.mockito.Mockito.times;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.credit_card.credit_line.dtos.request.MovementRequest;
import com.credit_card.credit_line.dtos.response.MovementResponse;
import com.credit_card.credit_line.service.MovementService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MovementController.class)
public class MovementControllerTest {

    @MockitoBean
    private MovementService service;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void movementController_createMovement_registeredMovement() throws Exception{
        // Arrange
        MovementRequest request = new MovementRequest(
            20.00,
            "DEBIT",
            "payment",
            "SOL",
            "FALABELLA RETAIL S.A.C.",
            "ECOMMERCE",
            1);

        MovementResponse response = new MovementResponse(
            1,
            20.00,
            20.00,
            1.00,
            LocalDateTime.now().toString(),
            "DEBIT",
            "payment",
            "SOL",
            "SOL",
            "FALABELLA RETAIL S.A.C.",
            "ECOMMERCE",
            1);

        Map<String, Object> responseObject = new HashMap<>();
        responseObject.put("responseMessage", "Movement succesfuly registered");
        responseObject.put("responseCreditAmount", 1000.00);
        responseObject.put("responseCurrentAmount", 780.00);
        responseObject.put("response", response);

        Mockito.when(service.createMovement(Mockito.any(MovementRequest.class))).thenReturn(responseObject);    
        mvc.perform(post("/api/v1/movement/create").contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.responseMessage").value("Movement succesfuly registered"))
            .andExpect(jsonPath("$.responseCreditAmount").value(1000.0))
            .andExpect(jsonPath("$.responseCurrentAmount").value(780.0));
        Mockito.verify(service, times(1)).createMovement(request);
    }

    @Test
    void movementController_getMovementsByType_registeredMovement() throws Exception{
        String operationType = "DEBIT";
        int creditLinId = 1;
        String json = """
                {
                    "operationtype": "DEBIT"
                }
                """;

        MovementResponse response = new MovementResponse(
            1,
            20.00,
            20.00,
            1.00,
            LocalDateTime.now().toString(),
            "DEBIT",
            "payment",
            "SOL",
            "SOL",
            "FALABELLA RETAIL S.A.C.",
            "ECOMMERCE",
            1);

        List<MovementResponse> result = new ArrayList<>();
        result.add(response);

        Mockito.when(service.getMovementsByType(creditLinId, operationType)).thenReturn(result);    
        mvc.perform(get("/api/v1/movement/type-report/{creditLineId}", creditLinId).contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].movementId").value(1));
        Mockito.verify(service, times(1)).getMovementsByType(creditLinId, operationType);
    }
}
