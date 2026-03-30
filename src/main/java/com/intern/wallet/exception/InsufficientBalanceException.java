package com.intern.wallet.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() { super("Insufficient balance"); }
}
