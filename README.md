# 영화 예매 시스템 (java-movie-precourse)

영화 예매 도메인을 바탕으로 좌석 선택, 중복 시간 검증, 다양한 할인/포인트/결제수단 규칙을 적용해 총 결제 금액을 계산하는 프로젝트입니다.

### 목차
- 요구사항 TL;DR
- 가격 · 할인 규칙 표
- 계산 순서(1페이지 요약)
- 중복 시간/좌석 규칙
- 구현 기능 목록(=커밋 단위)
- 구현 전략 및 패키지 구조
- 커밋 컨벤션 · 예시
- 개발/실행 가이드
- AI 도구 활용 기록

## 요구사항 TL;DR
- 여러 상영 동시 예매 가능하나, 시간이 겹치면 불가
- 좌석 표기: 행(알파벳)+열(숫자) 예) `A1`, `C3`, `E4`
- 이미 예약된 좌석은 선택 불가
- 포인트는 결제 금액에서 직접 차감 후, 결제수단 할인 적용

## 가격 · 할인 규칙 표

좌석 기본가

| 등급 | 가격 |
| --- | --- |
| S | 18,000원 |
| A | 15,000원 |
| B | 12,000원 |

할인 규칙

| 구분 | 조건 | 방식 |
| --- | --- | --- |
| 무비데이 | 매월 10/20/30일 상영 | 10% 비율 할인 |
| 시간 조건 | 시작 시각 < 11:00 또는 ≥ 20:00 | 2,000원 정액 할인 |
| 결제수단 | 포인트 차감 후 적용 | 카드 5% / 현금 2% |

참고: 무비데이와 시간 조건은 동시 적용 가능하며, 적용 순서가 중요합니다.

## 계산 순서(1페이지 요약)
1) 좌석 기본가 합계
2) 무비데이 10% 비율 할인
3) 시간 조건 2,000원 정액 할인
4) 포인트 차감 (최소 0 보장)
5) 결제수단 할인 (카드 5% / 현금 2%)
6) 최종 금액 하한 0원, 반올림 규칙: 원 단위

## 중복 시간/좌석 규칙
- 중복 시간: 각 `Screening`의 `[startAt, endAt)` 구간이 겹치면 함께 예매 불가
- 상영 시간 계산: 영화 러닝타임과 상영관 운영 시간으로 결정
- 좌석 중복: 동일 `Screening`에서 이미 예약된 좌석은 재선택 불가

## 구현할 기능 목록 (커밋 단위)
1) 도메인 모델 정의: `Movie`, `Screening`, `Auditorium`, `Seat`, `SeatGrade`, `Reservation`, `Payment`
2) 좌석 등급별 가격 정책(S/A/B)
3) 상영 시작/종료 계산(러닝타임 × 운영 시간)
4) 다중 예매 시 상영 시간 겹침 검증
5) 좌석 선택 및 예약 상태 검증
6) 무비데이 10% 비율 할인
7) 시간 조건 2,000원 정액 할인
8) 할인 동시 적용 순서 보장(무비데이 → 시간)
9) 포인트 차감(직접 차감, 최소 0)
10) 결제수단 할인(포인트 후, 카드 5%/현금 2%)
11) 총액 계산 경계 처리(음수 방지/반올림)
12) 예외/검증 정리(좌석 범위/보유 포인트 초과 등)
13) 입출력(콘솔 or 서비스 API)
14) 테스트(단위/시나리오/경계)

## 구현 전략 및 패키지 구조
- 도메인 우선 설계: 정책/규칙은 도메인 서비스로 격리
- 금액 타입 `Money`로 일관 관리, 음수 방지
- 할인 파이프라인: 기본가 → 비율 → 정액 → 포인트 → 결제수단
- 시간 겹침 규칙은 반개구간 `[start, end)`로 통일
- 예약 좌석은 `Screening` 단위로 관리

패키지 예시
```
com.example.movie
├─ common (Money, DateTimeInterval)
├─ movie (Movie, SeatGrade)
├─ schedule (Screening, Auditorium)
├─ reservation (Reservation, ReservationItem, SeatId)
├─ pricing (PriceCalculator, DiscountPolicy, MovieDayPolicy, TimePolicy)
└─ payment (PaymentMethod, PaymentProcessor)
```

## 커밋 컨벤션 · 예시 (AngularJS)
- 형식: `<type>(scope): <subject>`
- type: `feat`(기능), `fix`(버그), `refactor`, `test`, `docs`, `chore`
- 예시
  - `feat(pricing): implement movie-day percentage discount`
  - `feat(pricing): apply time-based flat discount after movie-day`
  - `feat(reservation): prevent overlapping screenings in a single booking`
  - `fix(pricing): prevent negative totals when applying discounts`
  - `docs(readme): add implementation plan and commit strategy`

예상 커밋 시퀀스
1. `docs(readme): 기능 목록과 구현 전략 추가`
2. `feat(model): 핵심 도메인 모델 정의 (Movie, Screening, Seat 등)`
3. `feat(pricing): 좌석 등급 가격 및 Money 값 객체 추가`
4. `feat(scheduling): 러닝타임/운영시간 기반 시작·종료 계산 구현`
5. `feat(reservation): 예약 좌석 검증 및 중복 선택 방지`
6. `feat(reservation): 다중 예매 시 상영 시간 겹침 차단`
7. `feat(discount): 무비데이 10% 비율 할인 구현`
8. `feat(discount): 시간 조건 2,000원 정액 할인 구현`
9. `feat(pricing): 할인 적용 순서 강제 및 파이프라인 구성`
10. `feat(points): 포인트 차감 로직 적용`
11. `feat(payment): 결제수단 할인(카드 5%, 현금 2%) 적용`
12. `test(pricing): 날짜·시간 경계 테스트 추가`
13. `refactor(*): 네이밍 및 가드 절 정리`
14. `docs(readme): AI 활용 방식과 학습 내용 기록`

## 개발/실행 가이드
- 요구: JDK 17+, Gradle
- 테스트: `./gradlew test`
- 실행: 콘솔 UI가 있을 경우 `./gradlew run` (추가 안내 예정)

## AI 도구 활용 기록
- 활용: 요구 해석, 구조 설계, 커밋 단위 제안
- 코드 영향: 가격/할인 파이프라인, 시간 겹침 규칙, 금액 타입 전략(README 단계)
- 학습: 할인 우선순위(비율 → 정액 → 포인트 → 결제수단), 음수 방지, 경계값 중요성

---
본 README는 구현 전 계획 수립 문서이며, 진행에 따라 업데이트됩니다.