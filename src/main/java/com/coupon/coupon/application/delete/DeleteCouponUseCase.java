package com.coupon.coupon.application.delete;

import com.coupon.coupon.domain.model.Coupon;
import com.coupon.coupon.domain.repository.CouponRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class DeleteCouponUseCase {

    private final CouponRepository couponRepository;

    public DeleteCouponUseCase(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional
    public void execute(UUID id) {
        Coupon coupon = findCouponOrThrow(id);
        coupon.delete();
        couponRepository.save(coupon);
    }

    private Coupon findCouponOrThrow(UUID id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cupom não encontrado com id: " + id));
    }
}
