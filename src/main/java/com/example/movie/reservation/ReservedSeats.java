package com.example.movie.reservation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Screening 단위의 예약 좌석 집합.
 */
public final class ReservedSeats {
    private final Set<SeatId> reserved;

    public ReservedSeats() {
        this.reserved = new HashSet<>();
    }

    public boolean isAvailable(SeatId seatId) {
        return !reserved.contains(seatId);
    }

    public void reserve(SeatId seatId) {
        if (!isAvailable(seatId)) {
            throw new IllegalStateException("seat already reserved: " + seatId);
        }
        reserved.add(seatId);
    }

    public Set<SeatId> snapshot() {
        return Collections.unmodifiableSet(reserved);
    }
}


