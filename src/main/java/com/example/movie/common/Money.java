package com.example.movie.common;

import java.util.Objects;

/**
 * 금액 표현 객체. 한국 원화(KRW)를 long 정수로 보관합니다.
 * 불변(Immutable)으로 설계되어 금액 연산 시 항상 새 인스턴스를 반환합니다.
 * - percentageOff: 비율 할인 적용 (예: 10% 할인)
 * - applyPercent: 비율 금액 계산 (예: 총액의 5%를 계산)
 * - minusWon: 정액 차감 (예: 2,000원 차감)
 * 금액은 0원 미만으로 내려가지 않도록 방어합니다.
 */
public final class Money {
    private final long amountWon;

    private Money(long amountWon) {
        this.amountWon = Math.max(0L, amountWon);
    }

    public static Money ofWon(long amountWon) {
        return new Money(amountWon);
    }

    public long asWon() {
        return amountWon;
    }

    public Money plus(Money other) {
        return new Money(this.amountWon + other.amountWon);
    }

    public Money minus(Money other) {
        long result = this.amountWon - other.amountWon;
        return new Money(result);
    }

    public Money minusWon(long won) {
        long result = this.amountWon - won;
        return new Money(result);
    }

    public Money percentageOff(double percent) {
        if (percent <= 0) {
            return this;
        }
        double discounted = amountWon * (1.0 - percent / 100.0);
        long rounded = Math.round(discounted);
        return new Money(rounded);
    }

    public Money applyPercent(double percent) {
        if (percent <= 0) {
            return Money.ofWon(0);
        }
        long rounded = Math.round(amountWon * (percent / 100.0));
        return new Money(rounded);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Money)) {
            return false;
        }
        Money money = (Money) o;
        return amountWon == money.amountWon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amountWon);
    }

    @Override
    public String toString() {
        return amountWon + "원";
    }
}


