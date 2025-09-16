package com.example.movie.pricing;

import com.example.movie.common.Money;
import com.example.movie.schedule.Screening;
import java.time.LocalDate;

/**
 * 무비데이 할인 정책.
 * 매월 10일/20일/30일에 시작하는 상영에 비율(%) 할인을 적용합니다.
 * 우선순위: 비율 할인(무비데이) → 정액 할인(시간)
 */
public final class MovieDayPolicy implements DiscountPolicy {
    private final double percent;

    public MovieDayPolicy(double percent) {
        this.percent = percent;
    }

    @Override
    public Money apply(Money current, Screening screening) {
        LocalDate d = screening.startsAt().toLocalDate();
        int day = d.getDayOfMonth();
        boolean isMovieDay = day == 10 || day == 20 || day == 30;
        if (!isMovieDay) {
            return current;
        }
        return current.percentageOff(percent);
    }
}


