package com.coupon.coupon.domain.model;

import com.coupon.coupon.domain.exception.CouponAlreadyDeletedException;
import com.coupon.coupon.domain.exception.InvalidCouponCodeException;
import com.coupon.coupon.domain.exception.InvalidDiscountValueException;
import com.coupon.coupon.domain.exception.PastExpirationDateException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Coupon {

    private static final int CODE_LENGTH = 6;
    private static final BigDecimal MIN_DISCOUNT_VALUE = new BigDecimal("0.5");

    private UUID id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private LocalDateTime expirationDate;
    private boolean published;
    private boolean redeemed;
    private LocalDateTime deletedAt;

    public Coupon() {
    }

    public static Coupon create(String code, String description, BigDecimal discountValue,
                                LocalDateTime expirationDate, boolean published) {
        String sanitizedCode = sanitizeCode(code);
        validateCodeLength(sanitizedCode);
        validateDiscountValue(discountValue);
        validateExpirationDate(expirationDate);

        Coupon coupon = new Coupon();
        coupon.id = UUID.randomUUID();
        coupon.code = sanitizedCode;
        coupon.description = description;
        coupon.discountValue = discountValue;
        coupon.expirationDate = expirationDate;
        coupon.published = published;
        coupon.redeemed = false;
        coupon.deletedAt = null;
        return coupon;
    }

    // código aceito só alfanumérico; especiais são removidos antes de validar tamanho
    private static String sanitizeCode(String code) {
        if (code == null || code.isBlank()) {
            return "";
        }
        return code.replaceAll("[^a-zA-Z0-9]", "");
    }

    private static void validateCodeLength(String sanitizedCode) {
        if (sanitizedCode.length() != CODE_LENGTH) {
            throw new InvalidCouponCodeException(
                    "O código do cupom deve ter exatamente " + CODE_LENGTH + " caracteres alfanuméricos após sanitização. " +
                            "Recebido: '" + sanitizedCode + "' (" + sanitizedCode.length() + " caracteres).");
        }
    }

    private static void validateDiscountValue(BigDecimal discountValue) {
        if (discountValue == null || discountValue.compareTo(MIN_DISCOUNT_VALUE) < 0) {
            throw new InvalidDiscountValueException(
                    "O valor de desconto deve ser no mínimo " + MIN_DISCOUNT_VALUE + ". Recebido: " + discountValue);
        }
    }

    private static void validateExpirationDate(LocalDateTime expirationDate) {
        if (expirationDate == null || expirationDate.isBefore(LocalDateTime.now())) {
            throw new PastExpirationDateException(
                    "A data de expiração não pode ser uma data passada. Recebida: " + expirationDate);
        }
    }

    public void delete() {
        if (this.deletedAt != null) {
            throw new CouponAlreadyDeletedException(
                    "O cupom com id '" + id + "' já foi deletado em " + this.deletedAt + ".");
        }
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
