package com.coupon.coupon.domain.model;

import com.coupon.coupon.domain.exception.CouponAlreadyDeletedException;
import com.coupon.coupon.domain.exception.InvalidCouponCodeException;
import com.coupon.coupon.domain.exception.InvalidDiscountValueException;
import com.coupon.coupon.domain.exception.PastExpirationDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Coupon - regras de domínio")
class CouponTest {

    private static final LocalDateTime FUTURE = LocalDateTime.now().plusDays(1);
    private static final String VALID_CODE = "ABC123";
    private static final String DESCRIPTION = "Descrição do cupom";
    private static final BigDecimal VALID_DISCOUNT = new BigDecimal("1.50");

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("cria cupom válido com todos os campos")
        void createsValidCoupon() {
            Coupon coupon = Coupon.create(VALID_CODE, DESCRIPTION, VALID_DISCOUNT, FUTURE, false);

            assertThat(coupon.getId()).isNotNull();
            assertThat(coupon.getCode()).isEqualTo("ABC123");
            assertThat(coupon.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(coupon.getDiscountValue()).isEqualByComparingTo(VALID_DISCOUNT);
            assertThat(coupon.getExpirationDate()).isEqualTo(FUTURE);
            assertThat(coupon.isPublished()).isFalse();
            assertThat(coupon.isRedeemed()).isFalse();
            assertThat(coupon.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("remove caracteres especiais do código e mantém 6 caracteres")
        void sanitizesCodeWithSpecialCharacters() {
            Coupon coupon = Coupon.create("AB-C-12@3", DESCRIPTION, VALID_DISCOUNT, FUTURE, true);

            assertThat(coupon.getCode()).isEqualTo("ABC123");
        }

        @Test
        @DisplayName("rejeita código com menos de 6 caracteres")
        void throwsWhenCodeTooShortAfterSanitization() {
            assertThatThrownBy(() -> Coupon.create("AB12", DESCRIPTION, VALID_DISCOUNT, FUTURE, false))
                    .isInstanceOf(InvalidCouponCodeException.class)
                    .hasMessageContaining("6 caracteres");
        }

        @Test
        @DisplayName("lança exceção quando código tem mais de 6 caracteres após sanitização")
        void throwsWhenCodeTooLongAfterSanitization() {
            assertThatThrownBy(() -> Coupon.create("ABC1234", DESCRIPTION, VALID_DISCOUNT, FUTURE, false))
                    .isInstanceOf(InvalidCouponCodeException.class)
                    .hasMessageContaining("6 caracteres");
        }

        @Test
        @DisplayName("lança exceção quando código vazio após sanitização")
        void throwsWhenCodeEmptyAfterSanitization() {
            assertThatThrownBy(() -> Coupon.create("---!!!", DESCRIPTION, VALID_DISCOUNT, FUTURE, false))
                    .isInstanceOf(InvalidCouponCodeException.class);
        }

        @Test
        @DisplayName("lança exceção quando desconto é menor que 0.5")
        void throwsWhenDiscountLessThanMinimum() {
            assertThatThrownBy(() -> Coupon.create(VALID_CODE, DESCRIPTION, new BigDecimal("0.49"), FUTURE, false))
                    .isInstanceOf(InvalidDiscountValueException.class)
                    .hasMessageContaining("0.5");
        }

        @Test
        @DisplayName("aceita desconto exatamente 0.5")
        void acceptsMinimumDiscount() {
            Coupon coupon = Coupon.create(VALID_CODE, DESCRIPTION, new BigDecimal("0.5"), FUTURE, false);
            assertThat(coupon.getDiscountValue()).isEqualByComparingTo("0.5");
        }

        @Test
        @DisplayName("lança exceção quando data de expiração é passada")
        void throwsWhenExpirationDateInPast() {
            LocalDateTime past = LocalDateTime.now().minusDays(1);
            assertThatThrownBy(() -> Coupon.create(VALID_CODE, DESCRIPTION, VALID_DISCOUNT, past, false))
                    .isInstanceOf(PastExpirationDateException.class)
                    .hasMessageContaining("data passada");
        }

        @Test
        @DisplayName("lança exceção quando data de expiração é null")
        void throwsWhenExpirationDateNull() {
            assertThatThrownBy(() -> Coupon.create(VALID_CODE, DESCRIPTION, VALID_DISCOUNT, null, false))
                    .isInstanceOf(PastExpirationDateException.class);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("soft delete só marca deletedAt, dados continuam lá")
        void softDeleteSetsDeletedAtAndKeepsData() {
            Coupon coupon = Coupon.create(VALID_CODE, DESCRIPTION, VALID_DISCOUNT, FUTURE, false);
            String codeBefore = coupon.getCode();
            String descriptionBefore = coupon.getDescription();

            coupon.delete();

            assertThat(coupon.isDeleted()).isTrue();
            assertThat(coupon.getDeletedAt()).isNotNull();
            assertThat(coupon.getCode()).isEqualTo(codeBefore);
            assertThat(coupon.getDescription()).isEqualTo(descriptionBefore);
        }

        @Test
        @DisplayName("não pode deletar duas vezes")
        void cannotDeleteTwice() {
            Coupon coupon = Coupon.create(VALID_CODE, DESCRIPTION, VALID_DISCOUNT, FUTURE, false);
            coupon.delete();

            assertThatThrownBy(coupon::delete)
                    .isInstanceOf(CouponAlreadyDeletedException.class)
                    .hasMessageContaining("já foi deletado");
        }
    }
}
