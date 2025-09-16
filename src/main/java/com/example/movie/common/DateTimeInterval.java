package com.example.movie.common;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 시간 구간을 나타내는 값 객체. 반개구간 [start, end) 표현을 사용합니다.
 * - overlaps: 두 시간 구간이 겹치는지 판정 (end는 포함하지 않음)
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


