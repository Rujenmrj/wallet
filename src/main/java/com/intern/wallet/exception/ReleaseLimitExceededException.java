package com.intern.wallet.exception;

public class ReleaseLimitExceededException extends RuntimeException {
    public ReleaseLimitExceededException() { super("Release exceeds reserved amount"); }
}
