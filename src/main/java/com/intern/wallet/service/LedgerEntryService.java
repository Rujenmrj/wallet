package com.intern.wallet.service;

import com.intern.wallet.constants.LedgerEntryType;
import com.intern.wallet.dto.response.TransactionEntry;
import com.intern.wallet.entity.LedgerEntry;
import com.intern.wallet.shared.dto.ApiResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.UUID;

public interface LedgerEntryService {
    BigDecimal getBalance(UUID walletId);
    boolean save(LedgerEntry ledgerEntry);
    boolean checkForDuplicate(UUID transactionId, LedgerEntryType entryType);
    ApiResponse<TransactionEntry> getTransaction(UUID walletId,UUID transactionId);
    Page<LedgerEntry> getHistory(UUID walletId, int page, int pageSize);
}
