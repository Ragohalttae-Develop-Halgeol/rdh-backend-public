package com.sfermions.entity.enums;

public enum ContractStatus {
    PENDING(0),      // 대기 상태
    REJECTED(-1),    // 거절된 상태
    ACCEPTED(1);     // 승인된 상태

    private final int value;

    ContractStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}