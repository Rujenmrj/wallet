package com.intern.wallet.dto.mapper;

import com.intern.wallet.dto.response.TransactionEntry;
import com.intern.wallet.entity.LedgerEntry;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionEntryMapper {

    /**
     * Maps a list of LedgerEntry entities to a list of TransactionEntry DTOs.
     */
    public static List<TransactionEntry> toResponse(List<LedgerEntry> ledgerEntries) {
        if (ledgerEntries == null) {
            return List.of();
        }

        return ledgerEntries.stream()
                .map(TransactionEntryMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public static TransactionEntry mapToDto(LedgerEntry entity) {
        TransactionEntry dto = new TransactionEntry();
         dto.setTransactionId(entity.getTransactionId());
         dto.setEntryType(entity.getEntryType());
         dto.setAmount(entity.getAmount());
         dto.setBalanceAfter(entity.getBalanceAfter());
        return dto;
    }
}