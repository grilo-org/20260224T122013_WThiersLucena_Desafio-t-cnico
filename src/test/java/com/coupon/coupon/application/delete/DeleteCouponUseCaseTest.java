package com.coupon.coupon.application.delete;

import com.coupon.api.ApiApplication;
import com.coupon.coupon.domain.exception.CouponAlreadyDeletedException;
import com.coupon.coupon.domain.model.Coupon;
import com.coupon.coupon.domain.repository.CouponRepository;
import com.coupon.coupon.application.create.CreateCouponUseCase;
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
@DisplayName("DeleteCouponUseCase - integração")
class DeleteCouponUseCaseTest {

    @Autowired
    private CreateCouponUseCase createCouponUseCase;

    @Autowired
    private DeleteCouponUseCase deleteCouponUseCase;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("soft delete com sucesso e mantém dados no banco")
    void deleteSuccessAndKeepsData() {
        Coupon created = createCouponUseCase.execute(
                "DEL123",
                "Cupom para deletar",
                new BigDecimal("5.00"),
                LocalDateTime.now().plusDays(30),
                false
        );

        deleteCouponUseCase.execute(created.getId());

        Coupon fromDb = couponRepository.findById(created.getId()).orElseThrow();
        assertThat(fromDb.getDeletedAt()).isNotNull();
        assertThat(fromDb.getCode()).isEqualTo("DEL123");
        assertThat(fromDb.getDescription()).isEqualTo("Cupom para deletar");
        assertThat(fromDb.getDiscountValue()).isEqualByComparingTo("5.00");
    }

    @Test
    @DisplayName("não pode deletar duas vezes")
    void cannotDeleteTwice() {
        Coupon created = createCouponUseCase.execute(
                "DUP456",
                "Cupom duplo",
                new BigDecimal("1.00"),
                LocalDateTime.now().plusDays(1),
                false
        );

        deleteCouponUseCase.execute(created.getId());

        assertThatThrownBy(() -> deleteCouponUseCase.execute(created.getId()))
                .isInstanceOf(CouponAlreadyDeletedException.class)
                .hasMessageContaining("já foi deletado");
    }

    @Test
    @DisplayName("lança quando cupom não encontrado")
    void throwsWhenNotFound() {
        assertThatThrownBy(() -> deleteCouponUseCase.execute(java.util.UUID.randomUUID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não encontrado");
    }
}
