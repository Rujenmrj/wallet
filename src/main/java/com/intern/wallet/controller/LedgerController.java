package com.intern.wallet.controller;

import com.intern.wallet.dto.request.CreditDebitRequest;
import com.intern.wallet.dto.response.CreditDebitResponse;
import com.intern.wallet.dto.response.TransactionEntry;
import com.intern.wallet.service.LedgerEntryService;
import com.intern.wallet.service.WalletService;
import com.intern.wallet.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transaction")
@Slf4j
@RequiredArgsConstructor
public class LedgerController {

    private final WalletService walletService;
    private final LedgerEntryService ledgerEntryService;


    /**
     * Api for crediting balance
     *
     * @param creditRequest requestBody
     * @return CreditResponse
     */
    @PostMapping("/credit")
    public ResponseEntity<ApiResponse<CreditDebitResponse>> creditWallet(
            @RequestBody CreditDebitRequest creditRequest) {
        log.info("credit transaction request: {}", creditRequest);
        return ResponseEntity.ok(walletService.creditWallet(creditRequest));
    }

    /**
     * Api for balance reserve
     *
     * @param reserveRequest
     * requestBody
     * @return reserveResponse
     */
    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse<CreditDebitResponse>> reserveBalance(
            @RequestBody CreditDebitRequest reserveRequest) {
        log.info("reserve transaction request: {}", reserveRequest);
        return ResponseEntity.ok(walletService.reserveBalance(reserveRequest));
    }

    /**
     * Api for balance reserve
     *
     * @param releaseRequest
     * requestBody
     * @return releaseResponse
     */
    @PostMapping("/release")
    public ResponseEntity<ApiResponse<CreditDebitResponse>> releaseBalance(
            @RequestBody CreditDebitRequest releaseRequest) {
        log.info("release transaction request: {}", releaseRequest);
        return ResponseEntity.ok(walletService.releaseBalance(releaseRequest));
    }

    /**
     * Api for debiting balance
     *
     * @param debitRequest requestBody
     * @return debitResponse
     */
    @PostMapping("/debit")
    public ResponseEntity<ApiResponse<CreditDebitResponse>> debitWallet(
            @RequestBody CreditDebitRequest debitRequest) {
        log.info("debit transaction request: {}", debitRequest);
        return ResponseEntity.ok(walletService.debitWallet(debitRequest));
    }

    /** Api for balance reversal
     *
     * @param reverseRequest
     * requestBody
     * @return reverseResponse
     */
    @PostMapping("/reverse")
    public ResponseEntity<ApiResponse<CreditDebitResponse>> reverseBalance(
            @RequestBody CreditDebitRequest reverseRequest) {
        log.info("reverse transaction request: {}", reverseRequest);
        return ResponseEntity.ok(walletService.reverseBalance(reverseRequest));
    }

    /** Api for credit balance hold
     *
     * @param holdCreditRequest
     * requestBody
     * @return holdCreditResponse
     */
    @PostMapping("/hold")
    public ResponseEntity<ApiResponse<CreditDebitResponse>> holdCreditBalance(
            @RequestBody CreditDebitRequest holdCreditRequest) {
        log.info("hold credit transaction request: {}", holdCreditRequest);
        return ResponseEntity.ok(walletService.holdCredit(holdCreditRequest));
    }

    /** Api for single transaction
     *
     * @param transactionId
     * pathVariable
     * @return transactionEntry
     */
    @GetMapping("{walletId}/{transactionId}")
    public ResponseEntity<ApiResponse<TransactionEntry>> getTransaction(@PathVariable UUID walletId, @PathVariable UUID transactionId){
        log.info("transaction detail request: transactionId: {}", transactionId);
        return ResponseEntity.ok(ledgerEntryService.getTransaction(walletId, transactionId));
    }

}
