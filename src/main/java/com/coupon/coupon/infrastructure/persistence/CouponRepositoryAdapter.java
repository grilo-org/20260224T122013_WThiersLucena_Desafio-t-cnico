package com.coupon.coupon.infrastructure.persistence;

import com.coupon.coupon.domain.model.Coupon;
import com.coupon.coupon.domain.repository.CouponRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CouponRepositoryAdapter implements CouponRepository {

    private final CouponJpaRepository jpaRepository;

    public CouponRepositoryAdapter(CouponJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Coupon save(Coupon coupon) {
        CouponJpaEntity entity = toEntity(coupon);
        CouponJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    private CouponJpaEntity toEntity(Coupon coupon) {
        CouponJpaEntity entity = new CouponJpaEntity();
        entity.setId(coupon.getId());
        entity.setCode(coupon.getCode());
        entity.setDescription(coupon.getDescription());
        entity.setDiscountValue(coupon.getDiscountValue());
        entity.setExpirationDate(coupon.getExpirationDate());
        entity.setPublished(coupon.isPublished());
        entity.setRedeemed(coupon.isRedeemed());
        entity.setDeletedAt(coupon.getDeletedAt());
        return entity;
    }

    private Coupon toDomain(CouponJpaEntity entity) {
        Coupon coupon = new Coupon();
        coupon.setId(entity.getId());
        coupon.setCode(entity.getCode());
        coupon.setDescription(entity.getDescription());
        coupon.setDiscountValue(entity.getDiscountValue());
        coupon.setExpirationDate(entity.getExpirationDate());
        coupon.setPublished(entity.isPublished());
        coupon.setRedeemed(entity.isRedeemed());
        coupon.setDeletedAt(entity.getDeletedAt());
        return coupon;
    }
}
