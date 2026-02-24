package com.coupon.coupon.api.controller;

import com.coupon.coupon.api.request.CreateCouponRequest;
import com.coupon.coupon.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("DomainException retorna 400 e mensagem")
    void handleDomainException() {
        DomainException ex = new DomainException("regra violada");
        ResponseEntity<Map<String, String>> response = handler.handleDomainException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("message", "regra violada");
    }

    @Test
    @DisplayName("IllegalArgumentException com 'não encontrado' retorna 404")
    void handleIllegalArgumentNotFound() {
        IllegalArgumentException ex = new IllegalArgumentException("Cupom não encontrado com id: x");
        ResponseEntity<Map<String, String>> response = handler.handleIllegalArgument(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("message", "Cupom não encontrado com id: x");
    }

    @Test
    @DisplayName("IllegalArgumentException sem 'não encontrado' retorna 400")
    void handleIllegalArgumentOther() {
        IllegalArgumentException ex = new IllegalArgumentException("outro argumento inválido");
        ResponseEntity<Map<String, String>> response = handler.handleIllegalArgument(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("message", "outro argumento inválido");
    }

    @Test
    @DisplayName("IllegalArgumentException com mensagem null retorna 400 com mensagem padrão")
    void handleIllegalArgumentNullMessage() {
        IllegalArgumentException ex = new IllegalArgumentException();
        ResponseEntity<Map<String, String>> response = handler.handleIllegalArgument(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("message", "Argumento inválido");
    }

    @Test
    @DisplayName("MethodArgumentNotValidException retorna 400 com erros concatenados")
    void handleValidation() throws Exception {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new CreateCouponRequest(), "createCouponRequest");
        bindingResult.addError(new FieldError("createCouponRequest", "code", "code é obrigatório"));
        bindingResult.addError(new FieldError("createCouponRequest", "discountValue", "deve ser no mínimo 0.5"));
        Method createMethod = CouponController.class.getDeclaredMethod("create", CreateCouponRequest.class);
        MethodParameter parameter = new MethodParameter(createMethod, 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidation(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("message");
        assertThat(response.getBody().get("message")).contains("code");
        assertThat(response.getBody().get("message")).contains("discountValue");
    }

    @Test
    @DisplayName("Exception genérica retorna 500")
    void handleUnexpected() {
        Exception ex = new RuntimeException("erro inesperado");
        ResponseEntity<Map<String, String>> response = handler.handleUnexpected(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("message", "Erro interno. Tente novamente mais tarde.");
    }
}
