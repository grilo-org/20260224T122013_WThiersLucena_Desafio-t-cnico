package com.coupon.coupon.application.create;

import com.coupon.api.ApiApplication;
import com.coupon.coupon.domain.model.Coupon;
import com.coupon.coupon.domain.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = ApiApplication.class)
@ActiveProfiles("test")
@DisplayName("CreateCouponUseCase - integração")
class CreateCouponUseCaseTest {

    @Autowired
    private CreateCouponUseCase createCouponUseCase;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("persiste cupom válido no banco")
    void createsAndPersistsValidCoupon() {
        LocalDateTime expiration = LocalDateTime.now().plusDays(10);
        Coupon created = createCouponUseCase.execute("XYZ789", "Cupom de teste", new BigDecimal("2.00"), expiration, false);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getCode()).isEqualTo("XYZ789");

        Coupon fromDb = couponRepository.findById(created.getId()).orElseThrow();
        assertThat(fromDb.getCode()).isEqualTo("XYZ789");
        assertThat(fromDb.getDescription()).isEqualTo("Cupom de teste");
        assertThat(fromDb.getDiscountValue()).isEqualByComparingTo("2.00");
    }

    @Test
    @DisplayName("sanitiza código com caracteres especiais e persiste com 6 caracteres")
    void sanitizesCodeAndPersists() {
        LocalDateTime expiration = LocalDateTime.now().plusDays(5);
        Coupon created = createCouponUseCase.execute("A1-B2-C3", "Desc", new BigDecimal("1.00"), expiration, true);

        assertThat(created.getCode()).isEqualTo("A1B2C3");
        Coupon fromDb = couponRepository.findById(created.getId()).orElseThrow();
        assertThat(fromDb.getCode()).isEqualTo("A1B2C3");
    }

    @Test
    @DisplayName("lança exceção quando código inválido após sanitização")
    void throwsWhenCodeInvalid() {
        LocalDateTime expiration = LocalDateTime.now().plusDays(1);
        assertThatThrownBy(() -> createCouponUseCase.execute("AB", "Desc", new BigDecimal("1.0"), expiration, false))
                .hasMessageContaining("6 caracteres");
    }

    @Test
    @DisplayName("lança exceção quando desconto menor que 0.5")
    void throwsWhenDiscountTooLow() {
        LocalDateTime expiration = LocalDateTime.now().plusDays(1);
        assertThatThrownBy(() -> createCouponUseCase.execute("ABC123", "Desc", new BigDecimal("0.1"), expiration, false))
                .hasMessageContaining("0.5");
    }

    @Test
    @DisplayName("data de expiração no passado falha")
    void throwsWhenExpirationPast() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);
        assertThatThrownBy(() -> createCouponUseCase.execute("ABC123", "Desc", new BigDecimal("1.0"), past, false))
                .hasMessageContaining("data passada");
    }
}
