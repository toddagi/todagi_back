package com.example.emotionbot.api.dailySummary.entity;

public enum Feeling {
    ANGRY(0),ANNOY(1),SLEEPY(2),GOOD(3),HAPPY(4);
    private final int value;

    Feeling(int value) {
        this.value = value;
    }

    public static Feeling fromValue(int value) {
        return switch (value) {
            case 0 -> ANGRY;
            case 1 -> ANNOY;
            case 2 -> SLEEPY;
            case 3 -> GOOD;
            case 4 -> HAPPY;
            default -> throw new IllegalArgumentException("Invalid value: " + value);
        };
    }
}

