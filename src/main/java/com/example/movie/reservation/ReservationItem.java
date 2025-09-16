package com.example.movie.reservation;

import com.example.movie.movie.SeatGrade;
import com.example.movie.schedule.Screening;
import java.util.List;

public final class ReservationItem {
    private final Screening screening;
    private final List<SeatId> seats;
    private final List<SeatGrade> seatGrades;

    private ReservationItem(Screening screening, List<SeatId> seats, List<SeatGrade> seatGrades) {
        if (screening == null || seats == null || seatGrades == null) {
            throw new IllegalArgumentException("null not allowed");
        }
        if (seats.isEmpty() || seatGrades.isEmpty() || seats.size() != seatGrades.size()) {
            throw new IllegalArgumentException("seats and grades must be same non-empty size");
        }
        this.screening = screening;
        this.seats = List.copyOf(seats);
        this.seatGrades = List.copyOf(seatGrades);
    }

    public static ReservationItem of(Screening screening, List<SeatId> seats, List<SeatGrade> seatGrades) {
        return new ReservationItem(screening, seats, seatGrades);
    }

    public Screening screening() {
        return screening;
    }

    public List<SeatId> seats() {
        return seats;
    }

    public List<SeatGrade> seatGrades() {
        return seatGrades;
    }
}


