package com.intern.wallet.dto.response;

import com.intern.wallet.constants.LedgerEntryType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionEntry {
    private UUID transactionId;
    private LedgerEntryType entryType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
}
