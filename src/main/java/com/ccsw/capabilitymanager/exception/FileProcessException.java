package com.ccsw.capabilitymanager.exception;

public class FileProcessException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    public String message;

    public FileProcessException(String message) {
        super();
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
