package com.tms.domain;

import lombok.Getter;

/**
 * MoneyCurrency is an enum class, that contains the values of the ratio of currencies to the Belarusian ruble
 */

@Getter
public enum MoneyCurrency {
    USD(0.3042),
    EUR(0.287),
    RUS(29.6912),
    BYN(1);

    private final double VALUE;

    MoneyCurrency(double value) {
        this.VALUE = value;
    }
}
