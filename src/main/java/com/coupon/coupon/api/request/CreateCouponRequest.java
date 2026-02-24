package com.coupon.coupon.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Request para criação de cupom")
public class CreateCouponRequest {

    @NotBlank(message = "code é obrigatório")
    @Schema(description = "Código do cupom (alfanumérico; caracteres especiais serão removidos)", example = "ABC-123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @NotBlank(message = "description é obrigatória")
    @Schema(description = "Descrição do cupom", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @NotNull(message = "discountValue é obrigatório")
    @DecimalMin(value = "0.5", message = "discountValue deve ser no mínimo 0.5")
    @Schema(description = "Valor do desconto (mínimo 0.5)", example = "0.8", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal discountValue;

    @NotNull(message = "expirationDate é obrigatória")
    @Schema(description = "Data de expiração (não pode ser passada)", example = "2025-12-31T23:59:59", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime expirationDate;

    @Schema(description = "Se o cupom já está publicado", example = "false")
    private boolean published;

    public CreateCouponRequest() {
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
}
