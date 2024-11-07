package com.desafiotecnico.ponta.cesarlopes.Exceptions;

public class CustomException extends Exception {

    // Constructor with message only
    public CustomException(String message) {
        super(message);
    }

    // Constructor with message and cause
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with cause only
    public CustomException(Throwable cause) {
        super(cause);
    }

    // Constructor with message, cause, suppression enabled/disabled, and writableStackTrace
    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
