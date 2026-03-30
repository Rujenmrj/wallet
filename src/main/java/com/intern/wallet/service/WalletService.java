package com.intern.wallet.service;

import com.intern.wallet.constants.WalletStatus;
import com.intern.wallet.constants.WalletType;
import com.intern.wallet.dto.request.CreditDebitRequest;
import com.intern.wallet.dto.request.WalletRequest;
import com.intern.wallet.dto.response.CreditDebitResponse;
import com.intern.wallet.dto.response.TransactionHistoryResponse;
import com.intern.wallet.dto.response.WalletResponse;
import com.intern.wallet.shared.dto.ApiResponse;
import com.intern.wallet.shared.dto.PageResponse;

import java.math.BigDecimal;
import java.util.UUID;


public interface WalletService {
    ApiResponse<BigDecimal> getBalance(UUID walletId);
    ApiResponse<BigDecimal> getMerchantBalance(UUID walletId);

    ApiResponse<WalletResponse> createWallet(WalletRequest walletRequest);

    ApiResponse<WalletResponse> getWalletByUser(UUID userId);
    ApiResponse<WalletResponse> getMerchantWallet(UUID userId);

    ApiResponse<WalletResponse> getWallet(UUID walletId, WalletType walletType);

    ApiResponse<WalletResponse> toggleWalletStatus(UUID walletId, WalletStatus walletStatus);
    ApiResponse<WalletResponse> toggleMerchantWalletStatus(UUID walletId, WalletStatus walletStatus);

    ApiResponse<PageResponse<TransactionHistoryResponse>> getTransactionHistory(UUID walletId, int page, int pageSize);
    ApiResponse<PageResponse<TransactionHistoryResponse>> getMerchantTransactionHistory(UUID walletId, int page, int pageSize);

    ApiResponse<CreditDebitResponse> creditWallet(CreditDebitRequest creditRequest);
    ApiResponse<CreditDebitResponse> holdCredit(CreditDebitRequest creditRequest);

    ApiResponse<CreditDebitResponse> debitWallet(CreditDebitRequest debitRequest);

    ApiResponse<CreditDebitResponse> reserveBalance(CreditDebitRequest reserveRequest);

    ApiResponse<CreditDebitResponse> releaseBalance(CreditDebitRequest releaseRequest);

    ApiResponse<CreditDebitResponse> reverseBalance(CreditDebitRequest releaseRequest);

    void settleMerchantBalances();
}
