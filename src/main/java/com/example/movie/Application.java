package com.example.movie;

import com.example.movie.movie.Movie;
import com.example.movie.movie.SeatGrade;
import com.example.movie.payment.PaymentMethod;
import com.example.movie.reservation.SeatId;
import com.example.movie.schedule.Screening;
import com.example.movie.service.BookingService;
import com.example.movie.service.BookingServiceReceipt;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class Application {
    public static void main(String[] args) {
        runDemo();
    }

    private static void runDemo() {
        Movie movie = createMovie();
        Screening screening = createDemoScreening(movie);
        BookingService booking = new BookingService();
        ensureSeatsAvailable(booking, screening);
        BookingServiceReceipt receipt = doBooking(booking, screening);
        assertPositiveTotal(receipt);
    }

    private static Movie createMovie() {
        return Movie.of(
            "Blockbuster",
            Duration.ofMinutes(130),
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 12, 31)
        );
    }

    private static Screening createDemoScreening(Movie movie) {
        return Screening.of(movie, LocalDateTime.of(2025, 10, 20, 20, 15));
    }

    private static void ensureSeatsAvailable(BookingService booking, Screening screening) {
        boolean a1 = booking.isSeatAvailable(screening, SeatId.of('A', 1));
        if (!a1) {
            throw new IllegalStateException("좌석 A1 사용 불가");
        }
        boolean a2 = booking.isSeatAvailable(screening, SeatId.of('A', 2));
        if (!a2) {
            throw new IllegalStateException("좌석 A2 사용 불가");
        }
    }

    private static BookingServiceReceipt doBooking(BookingService booking, Screening screening) {
        return booking.book(
            screening,
            List.of(SeatId.of('A', 1), SeatId.of('A', 2)),
            List.of(SeatGrade.S, SeatGrade.A),
            3000L,
            PaymentMethod.CARD
        );
    }

    private static void assertPositiveTotal(BookingServiceReceipt receipt) {
        long total = receipt.totalAmount().asWon();
        if (total <= 0) {
            throw new IllegalStateException("총액 계산 오류");
        }
    }
}


