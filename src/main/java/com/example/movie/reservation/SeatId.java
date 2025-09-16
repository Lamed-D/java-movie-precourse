package com.example.movie.reservation;

import java.util.Objects;

/**
 * 좌석 식별자. 행(알파벳 A-Z) + 열(양의 정수)로 구성됩니다. 예) A1, C3
 */
public final class SeatId {
    private final char row; // A-Z
    private final int column; // 1..n

    private SeatId(char row, int column) {
        if (row < 'A' || row > 'Z') {
            throw new IllegalArgumentException("row must be A-Z");
        }
        if (column <= 0) {
            throw new IllegalArgumentException("column must be positive");
        }
        this.row = row;
        this.column = column;
    }

    public static SeatId of(char row, int column) {
        return new SeatId(row, column);
    }

    public String asString() {
        return String.valueOf(row) + column;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SeatId)) {
            return false;
        }
        SeatId seatId = (SeatId) o;
        return row == seatId.row && column == seatId.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return asString();
    }
}


