package com.example.movie.schedule;

import com.example.movie.common.DateTimeInterval;
import com.example.movie.movie.Movie;
import java.time.LocalDateTime;

/**
 * 상영 회차. 특정 영화의 시작 시각으로 정의되며,
 * 영화의 러닝타임으로 종료 시각이 결정됩니다.
 * - overlaps: 다른 상영 회차와 시간이 겹치는지 확인
 */
public final class Screening {
    private final Movie movie;
    private final LocalDateTime startsAt;
    private final DateTimeInterval interval;

    private Screening(Movie movie, LocalDateTime startsAt) {
        if (movie == null || startsAt == null) {
            throw new IllegalArgumentException("movie and startsAt required");
        }
        this.movie = movie;
        this.startsAt = startsAt;
        this.interval = DateTimeInterval.of(startsAt, startsAt.plus(movie.runningTime()));
        if (!movie.isInPeriod(startsAt.toLocalDate())) {
            throw new IllegalArgumentException("screening outside movie period");
        }
    }

    public static Screening of(Movie movie, LocalDateTime startsAt) {
        return new Screening(movie, startsAt);
    }

    public Movie movie() {
        return movie;
    }

    public LocalDateTime startsAt() {
        return startsAt;
    }

    public DateTimeInterval interval() {
        return interval;
    }

    public boolean overlaps(Screening other) {
        return this.interval.overlaps(other.interval);
    }
}


