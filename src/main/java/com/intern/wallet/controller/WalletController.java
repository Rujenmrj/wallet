package com.intern.wallet.controller;

import com.intern.wallet.constants.WalletStatus;
import com.intern.wallet.constants.WalletType;
import com.intern.wallet.dto.request.WalletRequest;
import com.intern.wallet.dto.response.TransactionHistoryResponse;
import com.intern.wallet.dto.response.WalletResponse;
import com.intern.wallet.service.WalletService;
import com.intern.wallet.shared.dto.ApiResponse;
import com.intern.wallet.shared.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@Slf4j
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    /**
     * Api for wallet creation
     *
     * @param walletCreateRequest requestBody
     * @return walletResponse
     */
    @PostMapping
    public ResponseEntity<ApiResponse<WalletResponse>> createWallet(
            @RequestBody WalletRequest walletCreateRequest) {
        log.info("wallet create request received: {}", walletCreateRequest);
        return ResponseEntity.ok(walletService.createWallet(walletCreateRequest));
    }

    /**
     * Api for user wallet retrieval
     *
     * @param walletId pathVariable
     * @return walletResponse
     */
    @GetMapping("/{walletId}")
    public ResponseEntity<ApiResponse<WalletResponse>> getUserWallet(@PathVariable UUID walletId) {
        log.info("get user Wallet request: {}", walletId);
        return ResponseEntity.ok(walletService.getWallet(walletId, WalletType.USER));
    }

    /**
     * Api for merchant wallet retrieval
     *
     * @param walletId pathVariable
     * @return walletResponse
     */
    @GetMapping("merchant/{walletId}")
    public ResponseEntity<ApiResponse<WalletResponse>> getMerchantWallet(@PathVariable UUID walletId) {
        log.info("get merchant Wallet request: {}", walletId);
        return ResponseEntity.ok(walletService.getWallet(walletId, WalletType.MERCHANT));
    }

    /**
     * Api for wallet retrieval
     *
     * @param userId pathVariable
     * @return walletResponse
     */
    @GetMapping("user/{userId}")
    public ResponseEntity<ApiResponse<WalletResponse>> getWalletByUser(@PathVariable UUID userId) {
        log.info("get Wallet By user request: {}", userId);
        return ResponseEntity.ok(walletService.getWalletByUser(userId));
    }

    /**
     * Api for balance Inquiry
     *
     * @param walletId pathVariable
     * @return BigDecimal
     */
    @GetMapping("/{walletId}/balance")
    public ResponseEntity<ApiResponse<BigDecimal>> getBalance(@PathVariable UUID walletId) {
        log.info("balance inquiry request received: {}", walletId);
        return ResponseEntity.ok(walletService.getBalance(walletId));
    }

    /** Api for Transaction History
     *
     * @param walletId
     * pathVariable
     * @param page
     * requestParam
     * @param pageSize
     * RequestParam
     * @return TransactionHistoryResponse
     */
    @GetMapping("/{walletId}/transactions")
    public ResponseEntity<ApiResponse<PageResponse<TransactionHistoryResponse>>> getTransactionHistory(
            @PathVariable UUID walletId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize){
        log.info("transaction history request received: {}", walletId);
        return ResponseEntity.ok(walletService.getTransactionHistory(walletId, page, pageSize));
    }

    /**
     * Api for wallet Status Toggle
     *
     * @param walletId
     * pathVariable
     * @param walletStatus
     * requestParam
     * @return walletResponse
     */
    @PatchMapping("{walletId}/status")
    public ResponseEntity<ApiResponse<WalletResponse>> updateWalletStatus(
            @PathVariable UUID walletId,
            @RequestParam WalletStatus walletStatus) {
        log.info("toggle Wallet request: \nwallet:{}, status:{}", walletId, walletStatus);
        return ResponseEntity.ok(walletService.toggleWalletStatus(walletId, walletStatus));
    }

}
