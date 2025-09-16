# 사용 가이드 (USAGE)

본 프로젝트는 콘솔 입출력을 제공하지 않으며, 테스트와 프로그램적 호출로 검증합니다.

## 1) 테스트로 검증 (권장)
- 사전조건: JDK 21+, Gradle Wrapper

Windows
```
.\\gradlew test
```
macOS/Linux
```
./gradlew test
```
성공 시 할인/포인트/결제수단, 시간 겹침/좌석 중복에 대한 핵심 시나리오가 통과합니다.

## 2) 프로그램적 사용 예시
`Application.main()`은 비어 있습니다. 아래와 같이 코드에서 직접 도메인을 사용해 검증할 수 있습니다.

```java
import com.example.movie.common.Money;
import com.example.movie.movie.Movie;
import com.example.movie.movie.SeatGrade;
import com.example.movie.payment.PaymentMethod;
import com.example.movie.pricing.PriceCalculator;
import com.example.movie.schedule.Screening;
import java.time.*;
import java.util.*;

public class DemoUsage {
    public static void main(String[] args) {
        Movie movie = Movie.of(
            "Sample", Duration.ofMinutes(120),
            LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31)
        );
        Screening screening = Screening.of(movie, LocalDateTime.of(2025, 10, 20, 20, 30)); // 무비데이+야간
        PriceCalculator calc = new PriceCalculator();

        Money total = calc.calculateTotal(
            screening,
            List.of(SeatGrade.S, SeatGrade.A), // 좌석 선택
            3000L,                             // 포인트 사용
            PaymentMethod.CARD                 // 결제수단
        );
        System.out.println("Total: " + total.asWon() + "원");
    }
}
```

컴파일/실행은 IDE에서 해당 클래스를 실행하거나, 별도 `main` 클래스로 프로젝트에 추가 후 `run` 태스크를 사용할 수 있습니다.

## 3) 검증 포인트 체크리스트
- 시간 겹침 금지: 동일 예매 내 상영 간 `[start, end)` 겹치면 예외
- 좌석 중복 금지: 같은 상영에서 이미 예약된 좌석 선택 시 예외
- 할인 순서: 기본가 → 무비데이(10%) → 시간(2,000원) → 포인트 → 결제수단(카드 5%/현금 2%)
- 금액 하한: 0원 이하로 내려가지 않음
- 스타일/요구사항: JDK 21, `System.exit()` 금지, else/switch/3항 금지, 들여쓰기 4 spaces 및 2단계 이하

## 4) 추가 참고
- 설치/환경 구성은 `INSTALL.md` 참고
- 기능/설계 개요는 `README.md` 참고
