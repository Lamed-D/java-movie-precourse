package com.example.movie.payment;

public enum PaymentMethod {
    CARD(5.0),
    CASH(2.0);

    private final double discountPercent;

    PaymentMethod(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double discountPercent() {
        return discountPercent;
    }
}


