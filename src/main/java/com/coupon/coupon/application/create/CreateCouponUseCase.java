package com.coupon.coupon.application.create;

import com.coupon.coupon.domain.model.Coupon;
import com.coupon.coupon.domain.repository.CouponRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class CreateCouponUseCase {

    private final CouponRepository couponRepository;

    public CreateCouponUseCase(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional
    public Coupon execute(String code, String description, BigDecimal discountValue,
                          LocalDateTime expirationDate, boolean published) {
        Coupon coupon = Coupon.create(code, description, discountValue, expirationDate, published);
        // TODO: em cenário de alta carga, avaliar cache por code para evitar duplicidade
        return couponRepository.save(coupon);
    }
}
