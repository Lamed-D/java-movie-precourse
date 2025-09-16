package com.example.movie.pricing;

import com.example.movie.common.Money;
import com.example.movie.schedule.Screening;

public interface DiscountPolicy {
    Money apply(Money current, Screening screening);
}


