package com.intern.wallet.repo;

import com.intern.wallet.constants.LedgerEntryType;
import com.intern.wallet.entity.LedgerEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {
    @Query("""
SELECT COALESCE(SUM(
    CASE
        WHEN l.entryType = 'CREDIT' THEN l.amount
        WHEN l.entryType = 'DEBIT' THEN -l.amount
    END
),0) AS Balance
FROM LedgerEntry l
WHERE l.wallet.id = :wallet
""")
    BigDecimal getBalance(@Param("wallet") UUID wallet);

    boolean existsLedgerEntryByTransactionIdAndEntryType(UUID transactionId, LedgerEntryType entryType);

    Page<LedgerEntry> findAllByWallet_IdAndEntryTypeIn(
            UUID walletId,
            List<LedgerEntryType> types,
            Pageable pageable
    );

    LedgerEntry findByWallet_IdAndTransactionIdAndEntryTypeIn(UUID walletId, UUID transactionId,List<LedgerEntryType> types);
}
