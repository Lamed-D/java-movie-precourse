package com.example.movie.common;

import java.util.Objects;

/**
 * Immutable money representation using KRW (won) as long.
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


