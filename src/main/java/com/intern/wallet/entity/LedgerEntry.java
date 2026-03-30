package com.intern.wallet.entity;

import com.intern.wallet.abstracts.AbstractEntity;
import com.intern.wallet.constants.LedgerEntryType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries",
uniqueConstraints = @UniqueConstraint(columnNames = {"transactionId", "entryType", "walletId"}))
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntry extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false)
    private UUID transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LedgerEntryType entryType;

    @Column(nullable = false)
    private BigDecimal balanceAfter;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    // ===== Immutable Enforcement =====
    @Override
    public void preUpdate() {
        throw new UnsupportedOperationException("Ledger entries are immutable and cannot be updated");
    }

    @PreRemove
    public void preventRemove() {
        throw new UnsupportedOperationException("Ledger entries are immutable and cannot be deleted");
    }
}
