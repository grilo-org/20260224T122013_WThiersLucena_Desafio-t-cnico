package com.coupon.coupon.api.controller;

import com.coupon.api.ApiApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("CouponController - integração")
class CouponControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /coupons retorna 201 e cupom criado")
    void createReturns201() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "code", "API123",
                "description", "Cupom API",
                "discountValue", 1.5,
                "expirationDate", LocalDateTime.now().plusDays(10).toString(),
                "published", false
        ));

        mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.code").value("API123"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("POST /coupons com dados inválidos retorna 400")
    void createInvalidReturns400() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "code", "AB",
                "description", "X",
                "discountValue", 0.1,
                "expirationDate", LocalDateTime.now().plusDays(1).toString()
        ));

        mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /coupons sem campos obrigatórios retorna 400 (Bean Validation)")
    void createWithMissingRequiredFieldsReturns400() throws Exception {
        String body = "{}";

        mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /coupons com code em branco retorna 400 (Bean Validation)")
    void createWithBlankCodeReturns400() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "code", "",
                "description", "Desc",
                "discountValue", 1.0,
                "expirationDate", LocalDateTime.now().plusDays(1).toString()
        ));

        mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("DELETE /coupons/{id} retorna 204 após criar cupom")
    void deleteReturns204() throws Exception {
        String createBody = objectMapper.writeValueAsString(Map.of(
                "code", "DEL999",
                "description", "Para deletar",
                "discountValue", BigDecimal.ONE,
                "expirationDate", LocalDateTime.now().plusDays(5).toString(),
                "published", false
        ));
        String createResponse = mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UUID id = UUID.fromString(objectMapper.readTree(createResponse).get("id").asText());

        mockMvc.perform(delete("/coupons/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /coupons/{id} com id inexistente retorna 404")
    void deleteNotFoundReturns404() throws Exception {
        mockMvc.perform(delete("/coupons/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE duas vezes retorna 400 na segunda")
    void deleteTwiceReturns400OnSecond() throws Exception {
        String createBody = objectMapper.writeValueAsString(Map.of(
                "code", "DUP888",
                "description", "Duplo",
                "discountValue", 2.0,
                "expirationDate", LocalDateTime.now().plusDays(3).toString(),
                "published", false
        ));
        String createResponse = mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UUID id = UUID.fromString(objectMapper.readTree(createResponse).get("id").asText());

        mockMvc.perform(delete("/coupons/" + id)).andExpect(status().isNoContent());
        mockMvc.perform(delete("/coupons/" + id)).andExpect(status().isBadRequest());
    }
}
