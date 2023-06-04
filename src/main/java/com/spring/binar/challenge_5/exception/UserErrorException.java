package com.spring.binar.challenge_5.exception;

public class UserErrorException extends RuntimeException {
    public UserErrorException(String msg) {
        super(msg);
    }
    public UserErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserErrorException(Throwable cause) {
        super(cause);
    }
}
