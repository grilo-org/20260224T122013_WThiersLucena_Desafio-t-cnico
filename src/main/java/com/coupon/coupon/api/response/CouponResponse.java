package com.coupon.coupon.api.response;

import com.coupon.coupon.domain.model.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Resposta com dados do cupom")
public class CouponResponse {

    @Schema(description = "Identificador único do cupom")
    private UUID id;

    @Schema(description = "Código do cupom (6 caracteres alfanuméricos)")
    private String code;

    @Schema(description = "Descrição do cupom")
    private String description;

    @Schema(description = "Valor do desconto")
    private BigDecimal discountValue;

    @Schema(description = "Data de expiração")
    private LocalDateTime expirationDate;

    @Schema(description = "Status do cupom: ACTIVE, INACTIVE, DELETED")
    private String status;

    @Schema(description = "Se está publicado")
    private boolean published;

    @Schema(description = "Se foi resgatado")
    private boolean redeemed;

    public CouponResponse() {
    }

    public static CouponResponse from(Coupon coupon) {
        CouponResponse response = new CouponResponse();
        response.setId(coupon.getId());
        response.setCode(coupon.getCode());
        response.setDescription(coupon.getDescription());
        response.setDiscountValue(coupon.getDiscountValue());
        response.setExpirationDate(coupon.getExpirationDate());
        response.setStatus(coupon.isDeleted() ? "DELETED" : "ACTIVE");
        response.setPublished(coupon.isPublished());
        response.setRedeemed(coupon.isRedeemed());
        return response;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
