package com.swiftcart.swiftcart.exception;

import lombok.Data;


public class CustomerException extends Exception {
    public CustomerException() {
    }

    public CustomerException(String message) {
        super(message);
    }
}
