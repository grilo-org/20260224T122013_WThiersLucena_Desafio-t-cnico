package com.coupon.coupon.domain.exception;

public class CouponAlreadyDeletedException extends DomainException {

    public CouponAlreadyDeletedException(String message) {
        super(message);
    }
}
