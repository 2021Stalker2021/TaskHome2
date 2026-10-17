package com.dmdev.taskhome2.exception;

public class DuplicateAddressException extends RuntimeException {
    public DuplicateAddressException(String message) {
        super(message);
    }
}
