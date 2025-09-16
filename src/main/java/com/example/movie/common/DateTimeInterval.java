package com.example.movie.common;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Half-open interval [start, end) for time overlap checks.
 */
public final class DateTimeInterval {
    private final LocalDateTime start;
    private final LocalDateTime end;

    private DateTimeInterval(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("start/end cannot be null");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("end must be after start");
        }
        this.start = start;
        this.end = end;
    }

    public static DateTimeInterval of(LocalDateTime start, LocalDateTime end) {
        return new DateTimeInterval(start, end);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean overlaps(DateTimeInterval other) {
        boolean startsBeforeOtherEnds = this.start.isBefore(other.end);
        boolean endsAfterOtherStarts = this.end.isAfter(other.start);
        return startsBeforeOtherEnds && endsAfterOtherStarts;
    }

    public Duration duration() {
        return Duration.between(start, end);
    }
}


