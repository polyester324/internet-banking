package com.tms.domain;

import lombok.Getter;

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
