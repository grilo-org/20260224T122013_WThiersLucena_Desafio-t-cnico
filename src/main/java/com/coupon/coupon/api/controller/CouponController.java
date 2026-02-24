package com.coupon.coupon.api.controller;

import com.coupon.coupon.api.request.CreateCouponRequest;
import com.coupon.coupon.api.response.CouponResponse;
import com.coupon.coupon.application.create.CreateCouponUseCase;
import com.coupon.coupon.application.delete.DeleteCouponUseCase;
import com.coupon.coupon.domain.model.Coupon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/coupons")
@Tag(name = "coupon", description = "Operações de cupons")
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;

    public CouponController(CreateCouponUseCase createCouponUseCase, DeleteCouponUseCase deleteCouponUseCase) {
        this.createCouponUseCase = createCouponUseCase;
        this.deleteCouponUseCase = deleteCouponUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar cupom")
    @ApiResponse(responseCode = "201", description = "Cupom criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<CouponResponse> create(@Valid @RequestBody CreateCouponRequest request) {
        Coupon coupon = createCouponUseCase.execute(
                request.getCode(),
                request.getDescription(),
                request.getDiscountValue(),
                request.getExpirationDate(),
                request.isPublished()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(CouponResponse.from(coupon));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cupom (soft delete)")
    @ApiResponse(responseCode = "204", description = "Deletado")
    @ApiResponse(responseCode = "400", description = "Já deletado ou inválido")
    @ApiResponse(responseCode = "404", description = "Não encontrado")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteCouponUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
