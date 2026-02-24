package com.coupon.coupon.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Exceções de domínio")
class DomainExceptionTest {

    @Test
    @DisplayName("DomainException com mensagem")
    void domainExceptionWithMessage() {
        DomainException ex = new DomainException("mensagem");
        assertThat(ex.getMessage()).isEqualTo("mensagem");
    }

    @Test
    @DisplayName("DomainException com mensagem e cause")
    void domainExceptionWithMessageAndCause() {
        Throwable cause = new RuntimeException("cause");
        DomainException ex = new DomainException("mensagem", cause);
        assertThat(ex.getMessage()).isEqualTo("mensagem");
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test
    @DisplayName("InvalidCouponCodeException")
    void invalidCouponCodeException() {
        InvalidCouponCodeException ex = new InvalidCouponCodeException("código inválido");
        assertThat(ex).isInstanceOf(DomainException.class);
        assertThat(ex.getMessage()).isEqualTo("código inválido");
    }

    @Test
    @DisplayName("InvalidDiscountValueException")
    void invalidDiscountValueException() {
        InvalidDiscountValueException ex = new InvalidDiscountValueException("desconto inválido");
        assertThat(ex).isInstanceOf(DomainException.class);
        assertThat(ex.getMessage()).isEqualTo("desconto inválido");
    }

    @Test
    @DisplayName("PastExpirationDateException")
    void pastExpirationDateException() {
        PastExpirationDateException ex = new PastExpirationDateException("data passada");
        assertThat(ex).isInstanceOf(DomainException.class);
        assertThat(ex.getMessage()).isEqualTo("data passada");
    }

    @Test
    @DisplayName("CouponAlreadyDeletedException")
    void couponAlreadyDeletedException() {
        CouponAlreadyDeletedException ex = new CouponAlreadyDeletedException("já deletado");
        assertThat(ex).isInstanceOf(DomainException.class);
        assertThat(ex.getMessage()).isEqualTo("já deletado");
    }
}
