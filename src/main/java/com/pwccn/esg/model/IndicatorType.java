package com.pwccn.esg.model;

public enum IndicatorType {

    // 定性的
    QUALITATIVE(0),
    // 定量的
    QUANTITATIVE(1);

    private final int value;

    IndicatorType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static IndicatorType parse(int value) {
        for (IndicatorType type : IndicatorType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }
}
