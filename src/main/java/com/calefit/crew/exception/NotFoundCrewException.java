package com.calefit.crew.exception;

public class NotFoundCrewException extends RuntimeException{

    public NotFoundCrewException() {
        super();
    }

    public NotFoundCrewException(String message) {
        super(message);
    }
}
