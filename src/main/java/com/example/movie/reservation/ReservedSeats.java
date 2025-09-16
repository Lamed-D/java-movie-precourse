package com.example.movie.reservation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 상영(Screening) 단위로 관리되는 예약 좌석 집합.
 * - isAvailable: 좌석이 예약 가능한지 확인
 * - reserve: 좌석을 예약 집합에 추가 (중복 시 예외)
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


