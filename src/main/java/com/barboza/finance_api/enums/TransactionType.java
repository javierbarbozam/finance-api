package com.barboza.finance_api.enums;

public enum TransactionType {
    INCOME, EXPENSE;

    public static TransactionType fromString(String value) {
        if (value == null) return null;
        
        for (TransactionType type : TransactionType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}