package com.intern.wallet.shared;

import org.springframework.http.HttpStatus;

public enum ResponseCodes {

    // General Validation Errors
    SUCCESS("000", "SUCCESS", HttpStatus.OK),
    MEMBER_NOT_FOUND("E400", "Member detail not found.", HttpStatus.NOT_FOUND),
    UNKNOWN_ERROR("E500", "The operation could not be completed due to an unexpected error. " +
            "Please try again later or contact support if the issue persists.", HttpStatus.INTERNAL_SERVER_ERROR),
    WALLET_NOT_FOUND("E404", "Wallet not found.", HttpStatus.NOT_FOUND),
    WALLET_FROZEN("E405","Wallet is Frozen", HttpStatus.LOCKED),
    INSUFFICIENT_BALANCE("E405", "Insufficient Balance", HttpStatus.NOT_ACCEPTABLE),
    TRANSACTION_NOT_FOUND("E404", "Transaction detail not found.", HttpStatus.NOT_FOUND),
    ACCOUNT_VALIDATION_FAILED("E401", "Account validation failed.", HttpStatus.BAD_REQUEST),
    DEBTOR_BANK_NOT_ENABLED("E402", "Debtor bank is not enabled.", HttpStatus.BAD_REQUEST),
    CREDITOR_BANK_NOT_ENABLED("E403", "Creditor bank is not enabled.", HttpStatus.BAD_REQUEST),
    ACCOUNT_VALIDATION_NOT_ENABLED("E405", "Account validation not enabled.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_ENABLED_TO_FETCH_BANK_LIST("E406", "Member not allowed to fetch bank list.", HttpStatus.BAD_REQUEST),
    INVALID_PAYMENT_INSTRUMENT("E407", "Invalid payment instrument.", HttpStatus.BAD_REQUEST),
    MOBILE_PAYMENT_NOT_ENABLED("E408", "Mobile payment not enabled.", HttpStatus.BAD_REQUEST),
    ACCOUNT_VALIDATION_SERVICE_NOT_ENABLED("E409", "Account validation service not available now.", HttpStatus.BAD_REQUEST),
    TECHNICAL_MEMBER_ACCOUNT_DETAILS_NOT_FOUND("E410", "Technical member account details not found.", HttpStatus.BAD_REQUEST),
    DUPLICATE_REQUEST("E411", "Duplicate request. Please, check and try again", HttpStatus.BAD_REQUEST),
    INVALID_TRANSACTION_LIMIT("E412", "Release exceeds reserved amount", HttpStatus.BAD_REQUEST),
    BANK_NOT_FOUND("E413", "Bank not found.", HttpStatus.BAD_REQUEST),
    CREDIT_STATUS_NOT_FOUND("E414", "Credit status not found. " +
            "Please, check statement and retry with manual options.",
            HttpStatus.BAD_REQUEST),
    REVERSAL_FAILED("E415", "Reversal failed.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_TECHNICAL("E416", "Member is not allowed to perform this operations", HttpStatus.BAD_REQUEST),
    ACCOUNT_NAME_MISMATCHED("AC401", "Account name mismatched.", HttpStatus.BAD_REQUEST),
    SWITCH_TIMEOUT("999","Timeout. Please verify destination bank before re-processing.",
            HttpStatus.INTERNAL_SERVER_ERROR);


    private final String code;
    private final String messageKey;
    private final HttpStatus defaultHttpStatus;

    ResponseCodes(String code, String messageKey, HttpStatus defaultHttpStatus) {
        this.code = code;
        this.messageKey = messageKey;
        this.defaultHttpStatus = defaultHttpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public HttpStatus getDefaultHttpStatus() {
        return defaultHttpStatus;
    }

}

