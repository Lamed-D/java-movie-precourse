package com.example.movie.schedule;

import com.example.movie.common.DateTimeInterval;
import com.example.movie.movie.Movie;
import java.time.LocalDateTime;

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


