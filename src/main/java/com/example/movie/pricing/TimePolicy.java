package com.example.movie.pricing;

import com.example.movie.common.Money;
import com.example.movie.schedule.Screening;
import java.time.LocalTime;

/**
 * 시간 조건 할인 정책.
 * 시작 시각이 11:00 이전이거나 20:00 이후(이상)면 정액(원) 할인을 적용합니다.
 * 무비데이(비율) 할인 다음에 적용되어야 합니다.
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


