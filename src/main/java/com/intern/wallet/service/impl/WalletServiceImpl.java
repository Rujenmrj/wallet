package com.intern.wallet.service.impl;

import com.intern.wallet.constants.LedgerEntryType;
import com.intern.wallet.constants.WalletStatus;
import com.intern.wallet.constants.WalletType;
import com.intern.wallet.dto.mapper.TransactionEntryMapper;
import com.intern.wallet.dto.mapper.WalletMapper;
import com.intern.wallet.dto.request.CreditDebitRequest;
import com.intern.wallet.dto.request.WalletRequest;
import com.intern.wallet.dto.response.CreditDebitResponse;
import com.intern.wallet.dto.response.TransactionEntry;
import com.intern.wallet.dto.response.TransactionHistoryResponse;
import com.intern.wallet.dto.response.WalletResponse;
import com.intern.wallet.entity.LedgerEntry;
import com.intern.wallet.entity.Wallet;
import com.intern.wallet.exception.*;
import com.intern.wallet.repo.WalletRepository;
import com.intern.wallet.service.LedgerEntryService;
import com.intern.wallet.service.WalletService;
import com.intern.wallet.shared.ResponseCodes;
import com.intern.wallet.shared.dto.ApiResponse;
import com.intern.wallet.shared.dto.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final LedgerEntryService ledgerService;

    // -------------------------------------------------------------------------
    // Wallet lifecycle
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public ApiResponse<WalletResponse> createWallet(WalletRequest request) {
        if (!WalletType.USER.equals(request.getType())) {
            Wallet holdingWallet = Wallet.builder()
                    .userId(request.getUserId())
                    .type(WalletType.HOLDING)
                    .status(WalletStatus.ACTIVE)
                    .build();
            walletRepository.save(holdingWallet);
        }

        Wallet wallet = Wallet.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .status(WalletStatus.ACTIVE)
                .build();

        return success(WalletMapper.toResponse(walletRepository.save(wallet)));
    }

    @Override
    public ApiResponse<WalletResponse> getWallet(UUID walletId, WalletType walletType) {
        Wallet wallet = walletRepository.findWalletByIdAndType(walletId, walletType)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        return success(WalletMapper.toResponse(wallet));
    }

    @Override
    @Transactional
    public ApiResponse<WalletResponse> toggleWalletStatus(UUID walletId, WalletStatus walletStatus) {
        Wallet wallet = walletRepository.findWalletByIdAndType(walletId, WalletType.USER)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        wallet.setStatus(walletStatus);
        return success(WalletMapper.toResponse(walletRepository.save(wallet)));
    }

    @Override
    @Transactional
    public ApiResponse<WalletResponse> toggleMerchantWalletStatus(UUID walletId, WalletStatus walletStatus) {
        Wallet merchantWallet = walletRepository.findWalletForUpdate(walletId, WalletType.MERCHANT)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        Wallet holdingWallet = walletRepository.findByUserIdForUpdate(merchantWallet.getUserId(), WalletType.HOLDING)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        merchantWallet.setStatus(walletStatus);
        holdingWallet.setStatus(walletStatus);
        walletRepository.save(holdingWallet);
        return success(WalletMapper.toResponse(walletRepository.save(merchantWallet)));
    }

    // -------------------------------------------------------------------------
    // Balance queries
    // -------------------------------------------------------------------------

    @Override
    public ApiResponse<WalletResponse> getWalletByUser(UUID userId) {
        Wallet wallet = walletRepository.findByUserIdAndType(userId, WalletType.USER)
                .orElseThrow(() -> new WalletNotFoundException(userId));
        return success(WalletMapper.toResponse(wallet));
    }

    @Override
    public ApiResponse<WalletResponse> getMerchantWallet(UUID userId) {
        Wallet wallet = walletRepository.findByUserIdAndType(userId, WalletType.MERCHANT)
                .orElseThrow(() -> new WalletNotFoundException(userId));
        return success(WalletMapper.toResponse(wallet));
    }

    @Override
    public ApiResponse<BigDecimal> getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findWalletByIdAndType(walletId, WalletType.USER)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        return success(wallet.getAvailableBalance());
    }

    @Override
    public ApiResponse<BigDecimal> getMerchantBalance(UUID walletId) {
        Wallet wallet = walletRepository.findWalletByIdAndType(walletId, WalletType.MERCHANT)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        return success(wallet.getAvailableBalance());
    }

    // -------------------------------------------------------------------------
    // Transaction history
    // -------------------------------------------------------------------------

    @Override
    public ApiResponse<PageResponse<TransactionHistoryResponse>> getTransactionHistory(
            UUID walletId, int page, int pageSize) {
        Wallet wallet = walletRepository.findWalletByIdAndType(walletId, WalletType.USER)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        return success(buildHistoryPage(wallet, page, pageSize));
    }

    @Override
    public ApiResponse<PageResponse<TransactionHistoryResponse>> getMerchantTransactionHistory(
            UUID walletId, int page, int pageSize) {
        Wallet wallet = walletRepository.findWalletByIdAndType(walletId, WalletType.MERCHANT)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        return success(buildHistoryPage(wallet, page, pageSize));
    }

    // -------------------------------------------------------------------------
    // User wallet mutations
    // -------------------------------------------------------------------------

    @Override
    @Transactional
    public ApiResponse<CreditDebitResponse> creditWallet(CreditDebitRequest request) {
        Wallet wallet = resolveAndValidateWallet(
                request.getWalletID(), request.getTransactionId(), LedgerEntryType.CREDIT);

        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        walletRepository.save(wallet);
        saveLedgerEntry(wallet, request.getTransactionId(), LedgerEntryType.CREDIT, request.getAmount());

        return success(buildCreditDebitResponse(wallet));
    }

    @Override
    @Transactional
    public ApiResponse<CreditDebitResponse> debitWallet(CreditDebitRequest request) {
        Wallet wallet = resolveAndValidateWallet(
                request.getWalletID(), request.getTransactionId(), LedgerEntryType.DEBIT);

        if (wallet.getReservedBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException();
        }
        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        wallet.setReservedBalance(wallet.getReservedBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);
        saveLedgerEntry(wallet, request.getTransactionId(), LedgerEntryType.DEBIT, request.getAmount());

        return success(buildCreditDebitResponse(wallet));
    }

    @Override
    @Transactional
    public ApiResponse<CreditDebitResponse> reserveBalance(CreditDebitRequest request) {
        Wallet wallet = resolveAndValidateWallet(
                request.getWalletID(), request.getTransactionId(), LedgerEntryType.RESERVE);

        if (wallet.getAvailableBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException();
        }
        wallet.setReservedBalance(wallet.getReservedBalance().add(request.getAmount()));
        walletRepository.save(wallet);
        saveLedgerEntry(wallet, request.getTransactionId(), LedgerEntryType.RESERVE, request.getAmount());

        return success(buildCreditDebitResponse(wallet));
    }

    @Override
    @Transactional
    public ApiResponse<CreditDebitResponse> releaseBalance(CreditDebitRequest request) {
        Wallet wallet = resolveAndValidateWallet(
                request.getWalletID(), request.getTransactionId(), LedgerEntryType.RELEASE);

        if (wallet.getReservedBalance().compareTo(request.getAmount()) < 0) {
            throw new ReleaseLimitExceededException();
        }
        wallet.setReservedBalance(wallet.getReservedBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);
        saveLedgerEntry(wallet, request.getTransactionId(), LedgerEntryType.RELEASE, request.getAmount());

        return success(buildCreditDebitResponse(wallet));
    }

    @Override
    @Transactional
    public ApiResponse<CreditDebitResponse> reverseBalance(CreditDebitRequest request) {
        Wallet wallet = resolveAndValidateWallet(
                request.getWalletID(), request.getTransactionId(), LedgerEntryType.REVERSAL);

        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        walletRepository.save(wallet);
        saveLedgerEntry(wallet, request.getTransactionId(), LedgerEntryType.REVERSAL, request.getAmount());

        return success(buildCreditDebitResponse(wallet));
    }

    // -------------------------------------------------------------------------
    // Merchant wallet mutations
    // -------------------------------------------------------------------------

    /**
     * Credits the HOLDING wallet's balance and marks the amount as pending on
     * the MERCHANT wallet. The duplicate check is done once, up front.
     * Previously resolveAndValidateMerchantWallet was called twice, triggering
     * the duplicate-transaction check twice for the same transactionId.
     */
    @Override
    @Transactional
    public ApiResponse<CreditDebitResponse> holdCredit(CreditDebitRequest request) {
        // BUG FIX: single duplicate check before touching either wallet
        if (ledgerService.checkForDuplicate(request.getTransactionId(), LedgerEntryType.CREDIT)) {
            throw new DuplicateTransactionException();
        }

        Wallet merchantWallet = walletRepository.findWalletForUpdate(request.getWalletID(), WalletType.MERCHANT)
                .orElseThrow(() -> new WalletNotFoundException(request.getWalletID()));
        Wallet holdingWallet = walletRepository.findByUserIdForUpdate(merchantWallet.getUserId(), WalletType.HOLDING)
                .orElseThrow(() -> new WalletNotFoundException(request.getWalletID()));

        if (WalletStatus.FROZEN.equals(holdingWallet.getStatus()) ||
                WalletStatus.FROZEN.equals(merchantWallet.getStatus())) {
            throw new WalletFrozenException();
        }

        holdingWallet.setBalance(holdingWallet.getBalance().add(request.getAmount()));
        merchantWallet.setPendingBalance(merchantWallet.getPendingBalance().add(request.getAmount()));
        walletRepository.save(holdingWallet);
        walletRepository.save(merchantWallet);
        saveLedgerEntry(merchantWallet, request.getTransactionId(), LedgerEntryType.CREDIT, request.getAmount());

        return success(buildCreditDebitResponse(merchantWallet));
    }

    /**
     * Settles all merchant pending balances:
     *   1. RESERVE the settlement amount in the HOLDING wallet (guards against double-spend)
     *   2. DEBIT the HOLDING wallet's reserved amount
     *   3. CREDIT the MERCHANT wallet and clear the pending balance
     * If any ledger save fails (returns false / throws), the enclosing
     * Transactional Annotation rolls back the whole unit — no partial state is left behind.
     * Previously the if-on-save blocks ran *rollback* logic when save SUCCEEDED,
     * which inverted the intended behavior entirely.
     */
    @Override
    @Transactional
    public void settleMerchantBalances() {
        List<Wallet> merchantWallets = walletRepository.findAllMerchantWalletForUpdate();

        for (Wallet merchantWallet : merchantWallets) {
            BigDecimal amount = merchantWallet.getPendingBalance();
            UUID txId = UUID.randomUUID();

            Wallet holdingWallet = walletRepository.findByUserIdForUpdate(merchantWallet.getUserId(), WalletType.HOLDING)
                    .orElseThrow(() -> new WalletNotFoundException(merchantWallet.getUserId()));

            // Step 1 — reserve in holding wallet (prevents double-spend during settlement)
            holdingWallet.setReservedBalance(holdingWallet.getReservedBalance().add(amount));
            walletRepository.save(holdingWallet);
            saveLedgerEntry(holdingWallet, txId, LedgerEntryType.RESERVE, amount);

            // Step 2 — debit holding wallet reserved funds
            holdingWallet.setReservedBalance(holdingWallet.getReservedBalance().subtract(amount));
            walletRepository.save(holdingWallet);
            saveLedgerEntry(holdingWallet, txId, LedgerEntryType.DEBIT, amount);

            // Step 3 — credit merchant wallet, clear pending
            merchantWallet.setPendingBalance(merchantWallet.getPendingBalance().subtract(amount));
            merchantWallet.setBalance(merchantWallet.getBalance().add(amount));
            walletRepository.save(merchantWallet);
            saveLedgerEntry(merchantWallet, txId, LedgerEntryType.SETTLEMENT, amount);
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /** Validates idempotency + lock + frozen-status for USER wallets. */
    private Wallet resolveAndValidateWallet(UUID walletId, UUID transactionId, LedgerEntryType type) {
        if (ledgerService.checkForDuplicate(transactionId, type)) {
            throw new DuplicateTransactionException();
        }
        Wallet wallet = walletRepository.findWalletForUpdate(walletId, WalletType.USER)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        if (WalletStatus.FROZEN.equals(wallet.getStatus())) {
            throw new WalletFrozenException();
        }
        return wallet;
    }

    /** Builds a paged transaction history response for any wallet. */
    private PageResponse<TransactionHistoryResponse> buildHistoryPage(Wallet wallet, int page, int pageSize) {
        Page<LedgerEntry> entryPage = ledgerService.getHistory(wallet.getId(), page, pageSize);
        List<TransactionEntry> entries = TransactionEntryMapper.toResponse(entryPage.getContent());

        TransactionHistoryResponse body = TransactionHistoryResponse.builder()
                .history(entries)
                .wallet(WalletMapper.toResponse(wallet))
                .build();

        return new PageResponse<>(
                entryPage.getNumber(),
                entryPage.getSize(),
                entryPage.getTotalElements(),
                entryPage.getTotalPages(),
                body);
    }

    /** Saves a ledger entry with the wallet's current post-operation balance. */
    private void saveLedgerEntry(Wallet wallet, UUID transactionId, LedgerEntryType type, BigDecimal amount) {
        ledgerService.save(LedgerEntry.builder()
                .wallet(wallet)
                .transactionId(transactionId)
                .entryType(type)
                .amount(amount)
                .balanceAfter(wallet.getAvailableBalance())
                .build());
    }

    /** Wraps any payload in a success ApiResponse. */
    private <T> ApiResponse<T> success(T payload) {
        return new ApiResponse<>(true, ResponseCodes.SUCCESS.getMessageKey(), payload);
    }

    private CreditDebitResponse buildCreditDebitResponse(Wallet wallet) {
        return CreditDebitResponse.builder()
                .walletId(wallet.getId())
                .userId(wallet.getUserId())
                .balance(wallet.getAvailableBalance())
                .build();
    }
}