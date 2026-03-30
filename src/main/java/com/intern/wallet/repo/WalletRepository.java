package com.intern.wallet.repo;

import com.intern.wallet.constants.WalletType;
import com.intern.wallet.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByUserIdAndType(UUID userId, WalletType type);

//    Optional<Wallet> findWalletById(UUID id);

    Optional<Wallet> findWalletByIdAndType(UUID id, WalletType type);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id = :walletId AND w.type = :walletType")
    Optional<Wallet> findWalletForUpdate(UUID walletId, WalletType walletType);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.userId = :userId AND w.type = :walletType")
    Optional<Wallet> findByUserIdForUpdate(UUID userId, WalletType walletType);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Wallet m WHERE m.type = 'MERCHANT' AND m.status = 'ACTIVE' AND m.pendingBalance > 0")
    List<Wallet> findAllMerchantWalletForUpdate();
}
