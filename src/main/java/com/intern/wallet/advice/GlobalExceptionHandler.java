package com.intern.wallet.advice;

import com.intern.wallet.exception.*;
import com.intern.wallet.shared.ResponseCodes;
import com.intern.wallet.shared.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WalletNotFoundException.class)
    public ApiResponse<?> handleNotFound(WalletNotFoundException e) {
        log.error(e.getMessage());
        return new ApiResponse<>(false, ResponseCodes.WALLET_NOT_FOUND.getMessageKey(),
                null, ResponseCodes.WALLET_NOT_FOUND.getCode());
    }

    @ExceptionHandler(WalletFrozenException.class)
    public ApiResponse<?> handleFrozen(WalletFrozenException e) {
        log.error(e.getMessage());
        return new ApiResponse<>(false, ResponseCodes.WALLET_FROZEN.getMessageKey(),
                null, ResponseCodes.WALLET_FROZEN.getCode());
    }

    @ExceptionHandler(DuplicateTransactionException.class)
    public ApiResponse<?> handleDuplicate(DuplicateTransactionException e) {
        log.error(e.getMessage());
        return new ApiResponse<>(false, ResponseCodes.DUPLICATE_REQUEST.getMessageKey(),
                null, ResponseCodes.DUPLICATE_REQUEST.getCode());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ApiResponse<?> handleInsufficient(InsufficientBalanceException e) {
        log.error(e.getMessage());
        return new ApiResponse<>(false, ResponseCodes.INSUFFICIENT_BALANCE.getMessageKey(),
                null, ResponseCodes.INSUFFICIENT_BALANCE.getCode());
    }

    @ExceptionHandler(ReleaseLimitExceededException.class)
    public ApiResponse<?> handleLimitExceeded(ReleaseLimitExceededException e) {
        log.error(e.getMessage());
        return new ApiResponse<>(false, ResponseCodes.INVALID_TRANSACTION_LIMIT.getMessageKey(),
                null, ResponseCodes.INVALID_TRANSACTION_LIMIT.getCode());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleGeneric(Exception e) {
        log.error("Unhandled exception", e);   // ← now you can actually see errors
        return new ApiResponse<>(false, ResponseCodes.UNKNOWN_ERROR.getMessageKey(),
                null, ResponseCodes.UNKNOWN_ERROR.getCode());
    }
}