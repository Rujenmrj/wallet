package com.intern.wallet.service.impl;

import com.intern.wallet.constants.LedgerEntryType;
import com.intern.wallet.dto.mapper.TransactionEntryMapper;
import com.intern.wallet.dto.response.TransactionEntry;
import com.intern.wallet.entity.LedgerEntry;
import com.intern.wallet.repo.LedgerEntryRepository;
import com.intern.wallet.service.LedgerEntryService;
import com.intern.wallet.shared.ResponseCodes;
import com.intern.wallet.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedgerEntryServiceImpl implements LedgerEntryService {

    private final LedgerEntryRepository ledgerEntryRepository;

    @Override
    public BigDecimal getBalance(UUID walletId) {
        return ledgerEntryRepository.getBalance(walletId);
    }

    @Override
    public boolean save(LedgerEntry ledgerEntry) {
        LedgerEntry entry = ledgerEntryRepository.save(ledgerEntry);
        return entry.getId() != null;
    }

    @Override
    public boolean checkForDuplicate(UUID transactionId, LedgerEntryType entryType) {
        return ledgerEntryRepository.existsLedgerEntryByTransactionIdAndEntryType(
                transactionId, entryType
        );
    }

    @Override
    public ApiResponse<TransactionEntry> getTransaction(UUID walletId,UUID transactionId) {
        List<LedgerEntryType> visibleTypes = List.of(
                LedgerEntryType.CREDIT,
                LedgerEntryType.DEBIT
        );
        LedgerEntry entry = ledgerEntryRepository
                .findByWallet_IdAndTransactionIdAndEntryTypeIn(walletId,transactionId,visibleTypes);

        return new ApiResponse<>(
                true,
                ResponseCodes.SUCCESS.getMessageKey(),
                TransactionEntryMapper.mapToDto(entry));
    }

    @Override
    public Page<LedgerEntry> getHistory(UUID walletId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        List<LedgerEntryType> visibleTypes = List.of(
                LedgerEntryType.CREDIT,
                LedgerEntryType.DEBIT,
                LedgerEntryType.REVERSAL
        );

        return ledgerEntryRepository.findAllByWallet_IdAndEntryTypeIn(walletId, visibleTypes, pageable);
    }
}
