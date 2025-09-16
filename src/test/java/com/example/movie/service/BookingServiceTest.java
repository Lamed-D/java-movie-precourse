package com.example.movie.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.movie.movie.Movie;
import com.example.movie.movie.SeatGrade;
import com.example.movie.payment.PaymentMethod;
import com.example.movie.reservation.ReservationItem;
import com.example.movie.reservation.SeatId;
import com.example.movie.schedule.Screening;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class BookingServiceTest {

    private Movie movie() {
        return Movie.of(
            "Demo",
            Duration.ofMinutes(100),
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 12, 31)
        );
    }

    @Test
    void quote_and_book_single_screening() {
        BookingService service = new BookingService();
        Screening s = Screening.of(movie(), LocalDateTime.of(2025, 10, 20, 20, 0)); // 무비데이+야간

        assertThat(service.isSeatAvailable(s, SeatId.of('A', 1))).isTrue();

        var quote = service.quote(s, List.of(SeatGrade.S, SeatGrade.B), 2000L, PaymentMethod.CARD);
        assertThat(quote.asWon()).isGreaterThan(0);

        BookingServiceReceipt receipt = service.book(
            s,
            List.of(SeatId.of('A', 1), SeatId.of('A', 2)),
            List.of(SeatGrade.S, SeatGrade.B),
            2000L,
            PaymentMethod.CARD
        );
        assertThat(receipt.totalAmount().asWon()).isGreaterThan(0);
        assertThat(service.isSeatAvailable(s, SeatId.of('A', 1))).isFalse();
    }

    @Test
    void boundary_times_11_00_and_20_00() {
        BookingService service = new BookingService();
        Screening before11 = Screening.of(movie(), LocalDateTime.of(2025, 3, 10, 10, 59));
        Screening at11 = Screening.of(movie(), LocalDateTime.of(2025, 3, 10, 11, 0));
        Screening at20 = Screening.of(movie(), LocalDateTime.of(2025, 3, 10, 20, 0));
        Screening before20 = Screening.of(movie(), LocalDateTime.of(2025, 3, 10, 19, 59));

        long base = service.quote(before11, List.of(SeatGrade.B), 0, null).asWon();
        long at11Won = service.quote(at11, List.of(SeatGrade.B), 0, null).asWon();
        long at20Won = service.quote(at20, List.of(SeatGrade.B), 0, null).asWon();
        long before20Won = service.quote(before20, List.of(SeatGrade.B), 0, null).asWon();

        // before11과 at20에는 시간 할인(2000)이 적용, at11과 before20에는 미적용
        assertThat(base).isEqualTo(at20Won);
        assertThat(before20Won).isEqualTo(at11Won);
        assertThat(base).isLessThan(before20Won);
    }

    @Test
    void book_batch_with_overlap_should_fail() {
        BookingService service = new BookingService();
        Screening s1 = Screening.of(movie(), LocalDateTime.of(2025, 6, 1, 10, 0));
        Screening s2 = Screening.of(movie(), LocalDateTime.of(2025, 6, 1, 10, 30));

        List<ReservationItem> items = List.of(
            ReservationItem.of(s1, List.of(SeatId.of('A', 1)), List.of(SeatGrade.A)),
            ReservationItem.of(s2, List.of(SeatId.of('B', 2)), List.of(SeatGrade.B))
        );

        assertThatThrownBy(() -> service.bookBatch(items, 0L, PaymentMethod.CASH))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("overlapping");
    }
}


