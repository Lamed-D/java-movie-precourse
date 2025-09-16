package com.example.movie.pricing;

import com.example.movie.common.Money;
import com.example.movie.schedule.Screening;

/**
 * 할인 정책 인터페이스. 현재 금액에 상영 정보를 고려해 할인 적용 후 금액을 반환합니다.
 */
public interface DiscountPolicy {
    Money apply(Money current, Screening screening);
}


