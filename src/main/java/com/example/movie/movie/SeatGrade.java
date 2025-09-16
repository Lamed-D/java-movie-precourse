package com.example.movie.movie;

import com.example.movie.common.Money;

/**
 * 좌석 등급과 기본 가격.
 */
public enum SeatGrade {
    S(18000),
    A(15000),
    B(12000);

    private final Money basePrice;

    SeatGrade(long won) {
        this.basePrice = Money.ofWon(won);
    }

    public Money basePrice() {
        return basePrice;
    }
}


