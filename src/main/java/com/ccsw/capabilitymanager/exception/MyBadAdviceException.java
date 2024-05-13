package com.ccsw.capabilitymanager.exception;

public class MyBadAdviceException extends RuntimeException {

 
	private static final long serialVersionUID = -56097300151634082L;
	public String message;

    public MyBadAdviceException(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
