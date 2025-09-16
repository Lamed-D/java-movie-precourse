package com.example.movie.service;

import com.example.movie.common.Money;
import com.example.movie.reservation.SeatId;
import java.util.List;

/**
 * 예매 완료 영수증: 최종 결제 금액과 좌석 목록을 제공.
 */
public final class BookingServiceReceipt {
    private final Money totalAmount;
    private final List<SeatId> seats;

    public BookingServiceReceipt(Money totalAmount, List<SeatId> seats) {
        this.totalAmount = totalAmount;
        this.seats = seats;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public List<SeatId> seats() {
        return seats;
    }
}


