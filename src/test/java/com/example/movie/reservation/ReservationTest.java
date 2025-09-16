package com.example.movie.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.movie.movie.Movie;
import com.example.movie.movie.SeatGrade;
import com.example.movie.payment.PaymentMethod;
import com.example.movie.schedule.Screening;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReservationTest {

    private Movie movie() {
        return Movie.of(
            "Overlap",
            Duration.ofMinutes(90),
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 12, 31)
        );
    }

    @Test
    void prevent_overlapping_screenings_in_single_booking() {
        Reservation reservation = new Reservation();
        Screening s1 = Screening.of(movie(), LocalDateTime.of(2025, 10, 10, 10, 0));
        Screening s2 = Screening.of(movie(), LocalDateTime.of(2025, 10, 10, 10, 30)); // 겹침

        reservation.addItem(ReservationItem.of(s1, List.of(SeatId.of('A', 1)), List.of(SeatGrade.S)));

        assertThatThrownBy(() -> reservation.addItem(ReservationItem.of(s2, List.of(SeatId.of('B', 2)), List.of(SeatGrade.A))))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("overlapping");
    }

    @Test
    void prevent_double_booking_same_seat() {
        Reservation reservation = new Reservation();
        Screening s1 = Screening.of(movie(), LocalDateTime.of(2025, 10, 10, 13, 0));

        reservation.addItem(ReservationItem.of(s1, List.of(SeatId.of('A', 1)), List.of(SeatGrade.S)));

        assertThatThrownBy(() -> reservation.addItem(ReservationItem.of(s1, List.of(SeatId.of('A', 1)), List.of(SeatGrade.S))))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("seat already reserved");
    }

    @Test
    void total_points_and_payment_applied_on_grand_total() {
        Reservation reservation = new Reservation();
        Screening s1 = Screening.of(movie(), LocalDateTime.of(2025, 3, 1, 9, 0)); // 시간 조건
        Screening s2 = Screening.of(movie(), LocalDateTime.of(2025, 3, 1, 12, 0));

        reservation.addItem(ReservationItem.of(s1, List.of(SeatId.of('A', 1)), List.of(SeatGrade.B))); // 12000 → 시간 -2000 = 10000
        reservation.addItem(ReservationItem.of(s2, List.of(SeatId.of('B', 2)), List.of(SeatGrade.B))); // 12000

        // 항목 합계: 22000, 포인트 2000 차감 → 20000, 카드 5% 할인 → 19000
        assertThat(reservation.total(2000, PaymentMethod.CARD).asWon()).isEqualTo(19000);
    }
}


