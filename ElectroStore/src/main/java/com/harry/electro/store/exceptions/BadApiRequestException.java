package com.harry.electro.store.exceptions;

public class BadApiRequestException extends RuntimeException{
    public BadApiRequestException() {
        super("Bad API Request!");
    }
    public BadApiRequestException(String message) {
        super(message);
    }
}
