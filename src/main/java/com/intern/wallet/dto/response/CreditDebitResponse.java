package com.intern.wallet.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CreditDebitResponse {
    private UUID walletId;
    private UUID userId;
    private BigDecimal balance;
}
