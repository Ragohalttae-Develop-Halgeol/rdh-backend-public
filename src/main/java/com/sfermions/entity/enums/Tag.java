package com.sfermions.entity.enums;

public enum Tag {
    ACADEMICS(0),
    HOBBIES(1),
    TRAVEL(2),
    CAREER(3),
    CULTURE(4);

    private final int value;

    Tag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}