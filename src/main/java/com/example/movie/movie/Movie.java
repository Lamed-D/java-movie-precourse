package com.example.movie.movie;

import java.time.Duration;
import java.time.LocalDate;

public final class Movie {
    private final String title;
    private final Duration runningTime;
    private final LocalDate openDate;
    private final LocalDate closeDate;

    private Movie(String title, Duration runningTime, LocalDate openDate, LocalDate closeDate) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title required");
        }
        if (runningTime == null || runningTime.isNegative() || runningTime.isZero()) {
            throw new IllegalArgumentException("runningTime must be positive");
        }
        if (openDate == null || closeDate == null || closeDate.isBefore(openDate)) {
            throw new IllegalArgumentException("invalid showing period");
        }
        this.title = title;
        this.runningTime = runningTime;
        this.openDate = openDate;
        this.closeDate = closeDate;
    }

    public static Movie of(String title, Duration runningTime, LocalDate openDate, LocalDate closeDate) {
        return new Movie(title, runningTime, openDate, closeDate);
    }

    public String title() {
        return title;
    }

    public Duration runningTime() {
        return runningTime;
    }

    public boolean isInPeriod(LocalDate date) {
        return !date.isBefore(openDate) && !date.isAfter(closeDate);
    }
}


