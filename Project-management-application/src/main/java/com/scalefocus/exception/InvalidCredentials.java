package com.scalefocus.exception;

public class InvalidCredentials extends RuntimeException{
    public InvalidCredentials(String message) {
        super(message);
    }

    public InvalidCredentials(String message, Throwable cause) {
        super(message, cause);
    }
}
