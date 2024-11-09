package com.unihack.smart_usb.exception.auth;

public class MainException extends RuntimeException {
    public MainException() {
        super();
    }

    public MainException(String message) {
        super(message);
    }

    public String getResponseMessage() {
        return "{\"Error\":\"" + super.getMessage() + "\"}";
    }

}
