package com.credit_card.credit_line.advise;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.credit_card.credit_line.advice.GlobalExceptionHandler;
import com.credit_card.credit_line.controller.MovementController;
import com.credit_card.credit_line.dtos.request.MovementRequest;
import com.credit_card.credit_line.exception.CreditLineNotFoundException;
import com.credit_card.credit_line.exception.MovementNotFoundException;
import com.credit_card.credit_line.service.MovementService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = MovementController.class)
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MovementService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void movementNotFoundException() throws Exception {
         String operationType = "DEBIT";
        int creditLinId = 1;
        String json = """
                {
                    "operationtype": "DEBIT"
                }
                """;
        Mockito.when(service.getMovementsByType(creditLinId, operationType)).thenThrow(new MovementNotFoundException("Movement not found"));
        mvc.perform(get("/api/v1/movement/type-report/{creditLineId}", creditLinId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Movement not found"));
    }

    @Test
    void creditLineNotFoundException() throws Exception {
        MovementRequest request = new MovementRequest(
            20.00,
            "DEBIT",
            "payment",
            "SOL",
            "FALABELLA RETAIL S.A.C.",
            "ECOMMERCE",
            1);

        Mockito.when(service.createMovement(Mockito.any())).thenThrow(new CreditLineNotFoundException("Credit line service returned empty response, maybe entity was not found"));
        mvc.perform(post("/api/v1/movement/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Credit line service returned empty response, maybe entity was not found"));
    }

    @Test
    void methodArgumentNotValidException() throws Exception {
        MovementRequest request = new MovementRequest(
            0,
            "DEBIT",
            "payment",
            "SOL",
            "FALABELLA RETAIL S.A.C.",
            "ECOMMERCE",
            1);

        mvc.perform(post("/api/v1/movement/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void handleGeneralException() throws Exception {
        Mockito.when(service.createMovement(Mockito.any())).thenThrow(new RuntimeException("Random error"));
        mvc.perform(post("/api/v1/movement/created")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}
