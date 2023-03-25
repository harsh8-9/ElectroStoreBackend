package com.harry.electro.store.exceptions;

import org.springframework.http.HttpStatus;

/*
@author :-
        Harshal Bafna
 */
public class ResourceNotFoundException extends RuntimeException {
    private String message;
    public ResourceNotFoundException() {
        super("Resource Not Found!");
    }
    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
