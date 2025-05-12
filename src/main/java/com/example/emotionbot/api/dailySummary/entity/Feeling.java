package com.example.emotionbot.api.dailySummary.entity;

public enum Feeling {
    ANGRY(0), SAD(1), SLEEPY(2), EXCELLENT(3), HAPPY(4), UNKOWN(5);

    private final int value;

    Feeling(int value) {
        this.value = value;
    }

    public static Feeling fromValue(int value) {
        return switch (value) {
            case 0 -> ANGRY;
            case 1 -> SAD;
            case 2 -> SLEEPY;
            case 3 -> EXCELLENT;
            case 4 -> HAPPY;
            case 5-> UNKOWN;
        
            default -> throw new IllegalArgumentException("Invalid value: " + value);
        };
    }

    public static int toValue(String value) {
        return switch (value) {
            case "ANGRY" -> 0;
            case "SAD" -> 1;
            case "SLEEPY" -> 2;
            case "EXCELLENT" -> 3;
            case "HAPPY" -> 4;
            case "UNKOWN" -> 5;
            default -> throw new IllegalArgumentException("Invalid value: " + value);
        };
    }
}

