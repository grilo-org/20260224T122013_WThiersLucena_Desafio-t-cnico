package com.coupon.coupon.domain.exception;

public class PastExpirationDateException extends DomainException {

    public PastExpirationDateException(String message) {
        super(message);
    }
}
