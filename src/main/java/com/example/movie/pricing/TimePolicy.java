package com.example.movie.pricing;

import com.example.movie.common.Money;
import com.example.movie.schedule.Screening;
import java.time.LocalTime;

/**
 * 시간 조건: 오전 11시 이전 또는 오후 8시(20시) 이후 시작 상영은 2,000원 할인.
 */
public final class TimePolicy implements DiscountPolicy {
    private final long flatOffWon;

    public TimePolicy(long flatOffWon) {
        this.flatOffWon = flatOffWon;
    }

    @Override
    public Money apply(Money current, Screening screening) {
        LocalTime t = screening.startsAt().toLocalTime();
        boolean morning = t.isBefore(LocalTime.of(11, 0));
        boolean late = !t.isBefore(LocalTime.of(20, 0));
        if (!morning && !late) {
            return current;
        }
        return current.minusWon(flatOffWon);
    }
}


