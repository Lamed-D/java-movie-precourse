package com.example.movie.pricing;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.movie.common.Money;
import com.example.movie.movie.Movie;
import com.example.movie.movie.SeatGrade;
import com.example.movie.payment.PaymentMethod;
import com.example.movie.schedule.Screening;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class PriceCalculatorTest {

    private Movie movie() {
        return Movie.of(
            "Sample",
            Duration.ofMinutes(120),
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 12, 31)
        );
    }

    @Test
    void movieDay_then_time_discount_applies_in_order() {
        // 2025-10-20 20:30 → 무비데이(20일, 10%) + 시간(20시 이후, 2000원)
        Screening screening = Screening.of(movie(), LocalDateTime.of(2025, 10, 20, 20, 30));
        PriceCalculator calculator = new PriceCalculator();
        List<SeatGrade> seats = List.of(SeatGrade.S, SeatGrade.A); // 18000 + 15000 = 33000

        Money total = calculator.calculateTotal(screening, seats, 0, null);

        // 10% off → 29700, then -2000 → 27700
        assertThat(total.asWon()).isEqualTo(27700);
    }

    @Test
    void points_then_payment_discount_on_total() {
        Screening screening = Screening.of(movie(), LocalDateTime.of(2025, 3, 5, 10, 0)); // 시간 조건(11시 이전)
        PriceCalculator calculator = new PriceCalculator();
        List<SeatGrade> seats = List.of(SeatGrade.B, SeatGrade.B); // 12000*2 = 24000

        Money beforePayment = calculator.calculateTotal(screening, seats, 3000, null);
        assertThat(beforePayment.asWon()).isEqualTo(19000); // 시간 -2000 → 22000, 포인트 -3000 → 19000

        // 결제수단 할인: 카드 5%
        Money withCard = calculator.calculateTotal(screening, seats, 3000, PaymentMethod.CARD);
        // 24000 → -2000 → 22000 → -3000 → 19000 → -5% = 19000 - 950 = 18050
        assertThat(withCard.asWon()).isEqualTo(18050);
    }
}


