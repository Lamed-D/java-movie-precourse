package com.example.movie.service;

import com.example.movie.common.Money;
import com.example.movie.movie.SeatGrade;
import com.example.movie.payment.PaymentMethod;
import com.example.movie.pricing.PriceCalculator;
import com.example.movie.reservation.ReservationItem;
import com.example.movie.reservation.ReservedSeats;
import com.example.movie.reservation.SeatId;
import com.example.movie.schedule.Screening;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 사용자 친화적 예매 서비스 인터페이스.
 * - quote: 선택 좌석/할인/포인트/결제수단으로 예상 금액 산출
 * - isSeatAvailable: 좌석 예약 가능 여부 확인
 * - book: 단일 상영 예매 및 좌석 예약
 * - bookBatch: 여러 상영을 한 번에 예매 (시간 겹침 불가)
 */
public final class BookingService {
    private final Map<Screening, ReservedSeats> reservedByScreening;
    private final PriceCalculator calculator;

    public BookingService() {
        this.reservedByScreening = new HashMap<>();
        this.calculator = new PriceCalculator();
    }

    public Money quote(Screening screening, List<SeatGrade> seatGrades, long pointsUsed, PaymentMethod method) {
        return calculator.calculateTotal(screening, seatGrades, pointsUsed, method);
    }

    public boolean isSeatAvailable(Screening screening, SeatId seatId) {
        ReservedSeats bag = reservedByScreening.computeIfAbsent(screening, s -> new ReservedSeats());
        return bag.isAvailable(seatId);
    }

    public BookingServiceReceipt book(Screening screening, List<SeatId> seats, List<SeatGrade> seatGrades, long pointsUsed, PaymentMethod method) {
        validateSeatsInputs(seats, seatGrades);
        ensureAllAvailable(screening, seats);
        Money total = calculator.calculateTotal(screening, seatGrades, pointsUsed, method);
        reserveAll(screening, seats);
        return new BookingServiceReceipt(total, List.copyOf(seats));
    }

    public BookingServiceReceipt bookBatch(List<ReservationItem> items, long pointsUsed, PaymentMethod method) {
        validateNoOverlap(items);
        ensureAllAvailable(items);
        Money sum = Money.ofWon(0);
        for (ReservationItem item : items) {
            Money each = calculator.calculateTotal(item.screening(), item.seatGrades(), 0L, null);
            sum = sum.plus(each);
        }
        Money afterPoints = sum.minusWon(pointsUsed);
        Money finalAmount = applyPaymentDiscount(afterPoints, method);
        reserveAll(items);
        return new BookingServiceReceipt(finalAmount, collectAllSeats(items));
    }

    private void validateNoOverlap(List<ReservationItem> items) {
        int n = items.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                boolean overlap = items.get(i).screening().overlaps(items.get(j).screening());
                if (overlap) {
                    throw new IllegalStateException("overlapping screenings not allowed");
                }
            }
        }
    }

    private void ensureAllAvailable(Screening screening, List<SeatId> seats) {
        ReservedSeats bag = reservedByScreening.computeIfAbsent(screening, s -> new ReservedSeats());
        for (SeatId seatId : seats) {
            if (!bag.isAvailable(seatId)) {
                throw new IllegalStateException("seat already reserved: " + seatId);
            }
        }
    }

    private void ensureAllAvailable(List<ReservationItem> items) {
        for (ReservationItem item : items) {
            ensureAllAvailable(item.screening(), item.seats());
        }
    }

    private void reserveAll(Screening screening, List<SeatId> seats) {
        ReservedSeats bag = reservedByScreening.get(screening);
        for (SeatId seatId : seats) {
            bag.reserve(seatId);
        }
    }

    private void reserveAll(List<ReservationItem> items) {
        for (ReservationItem item : items) {
            reserveAll(item.screening(), item.seats());
        }
    }

    private List<SeatId> collectAllSeats(List<ReservationItem> items) {
        Set<SeatId> set = new HashSet<>();
        for (ReservationItem item : items) {
            set.addAll(item.seats());
        }
        return new ArrayList<>(set);
    }

    private Money applyPaymentDiscount(Money amount, PaymentMethod method) {
        if (method == null) {
            return amount;
        }
        long won = amount.applyPercent(method.discountPercent()).asWon();
        return amount.minusWon(won);
    }

    private void validateSeatsInputs(List<SeatId> seats, List<SeatGrade> seatGrades) {
        if (seats == null || seatGrades == null) {
            throw new IllegalArgumentException("null not allowed");
        }
        if (seats.isEmpty()) {
            throw new IllegalArgumentException("seats must not be empty");
        }
        if (seats.size() != seatGrades.size()) {
            throw new IllegalArgumentException("seats and grades size mismatch");
        }
    }
}


