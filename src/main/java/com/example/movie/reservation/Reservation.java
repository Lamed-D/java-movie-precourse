package com.example.movie.reservation;

import com.example.movie.common.Money;
import com.example.movie.payment.PaymentMethod;
import com.example.movie.pricing.PriceCalculator;
import com.example.movie.schedule.Screening;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Reservation {
    private final List<ReservationItem> items;
    private final Map<Screening, ReservedSeats> reservedByScreening;
    private final PriceCalculator priceCalculator;

    public Reservation() {
        this.items = new ArrayList<>();
        this.reservedByScreening = new HashMap<>();
        this.priceCalculator = new PriceCalculator();
    }

    public void addItem(ReservationItem item) {
        validateNoOverlap(item);
        ensureSeatsAvailable(item);
        items.add(item);
        reserveSeats(item);
    }

    public Money total(long pointsUsed, PaymentMethod method) {
        Money sum = Money.ofWon(0);
        for (ReservationItem item : items) {
            Money itemTotal = priceCalculator.calculateTotal(
                item.screening(),
                item.seatGrades(),
                0L,
                null
            );
            sum = sum.plus(itemTotal);
        }
        long pointsForAll = pointsUsed;
        Money afterPoints = sum.minusWon(pointsForAll);
        if (method == null) {
            return afterPoints;
        }
        // Payment discount applied to the grand total
        long discount = afterPoints.applyPercent(method.discountPercent()).asWon();
        return afterPoints.minusWon(discount);
    }

    private void validateNoOverlap(ReservationItem newItem) {
        for (ReservationItem existing : items) {
            boolean overlaps = existing.screening().overlaps(newItem.screening());
            if (overlaps) {
                throw new IllegalStateException("overlapping screenings not allowed");
            }
        }
    }

    private void ensureSeatsAvailable(ReservationItem item) {
        ReservedSeats reserved = reservedByScreening.computeIfAbsent(item.screening(), s -> new ReservedSeats());
        for (SeatId seatId : item.seats()) {
            if (!reserved.isAvailable(seatId)) {
                throw new IllegalStateException("seat already reserved: " + seatId);
            }
        }
    }

    private void reserveSeats(ReservationItem item) {
        ReservedSeats reserved = reservedByScreening.get(item.screening());
        for (SeatId seatId : item.seats()) {
            reserved.reserve(seatId);
        }
    }
}


