package com.tms.domain;

import lombok.Getter;
import java.math.BigDecimal;

/**
 * InvestmentTime is an enum class, that contains information about the types of investments, their interest and duration
 */
@Getter
public enum InvestmentTime {
    HALF_YEAR(BigDecimal.valueOf(0.04), 6),
    ONE_YEAR(BigDecimal.valueOf(0.1), 12),
    TWO_YEARS(BigDecimal.valueOf(0.23), 24);

    private final BigDecimal COEFFICIENT;
    private final int MONTHS_AMOUNT;

    InvestmentTime(BigDecimal coefficient, int monthsAmount) {
        this.COEFFICIENT = coefficient;
        this.MONTHS_AMOUNT = monthsAmount;
    }
}
