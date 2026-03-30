package com.intern.wallet.dto.request;

import com.intern.wallet.constants.LedgerEntryType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreditDebitRequest {
    private UUID transactionId;
    private LedgerEntryType entryType;
    private UUID walletID;
    private BigDecimal amount;
    private String description;
}
