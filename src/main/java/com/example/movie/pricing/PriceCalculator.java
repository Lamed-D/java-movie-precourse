package com.example.movie.pricing;

import com.example.movie.common.Money;
import com.example.movie.movie.SeatGrade;
import com.example.movie.payment.PaymentMethod;
import com.example.movie.schedule.Screening;
import java.util.List;

/**
 * 총액 계산기.
 * 적용 순서: 좌석 합계 → 무비데이(비율) → 시간(정액) → 포인트 → 결제수단(비율)
 * 각 단계는 0원 미만으로 내려가지 않도록 처리합니다.
 */
public final class PriceCalculator {
    private final DiscountPolicy movieDayPolicy;
    private final DiscountPolicy timePolicy;

    public PriceCalculator() {
        this.movieDayPolicy = new MovieDayPolicy(10.0);
        this.timePolicy = new TimePolicy(2000);
    }

    public Money calculateTotal(Screening screening, List<SeatGrade> seatGrades, long pointsUsed, PaymentMethod paymentMethod) {
        Money base = sumSeatPrices(seatGrades);
        Money afterMovieDay = movieDayPolicy.apply(base, screening);
        Money afterTime = timePolicy.apply(afterMovieDay, screening);
        Money afterPoints = afterTime.minusWon(pointsUsed);
        Money finalAmount = applyPaymentDiscount(afterPoints, paymentMethod);
        return finalAmount;
    }

    private Money sumSeatPrices(List<SeatGrade> seatGrades) {
        Money total = Money.ofWon(0);
        for (SeatGrade seatGrade : seatGrades) {
            total = total.plus(seatGrade.basePrice());
        }
        return total;
    }

    private Money applyPaymentDiscount(Money amount, PaymentMethod method) {
        if (method == null) {
            return amount;
        }
        double percent = method.discountPercent();
        long discount = amount.applyPercent(percent).asWon();
        return amount.minusWon(discount);
    }
}


