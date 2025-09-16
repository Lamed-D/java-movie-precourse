package com.example.movie.pricing;

import com.example.movie.common.Money;
import com.example.movie.schedule.Screening;
import java.time.LocalDate;

/**
 * 무비데이: 매월 10, 20, 30일에 상영되는 영화 10% 할인.
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


