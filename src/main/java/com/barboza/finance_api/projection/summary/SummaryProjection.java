package com.barboza.finance_api.projection.summary;

import java.math.BigDecimal;

import com.barboza.finance_api.enums.TransactionType;

public interface SummaryProjection {
    TransactionType getType();
    BigDecimal getTotal();
}
