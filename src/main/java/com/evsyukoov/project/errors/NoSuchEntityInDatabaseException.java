package com.evsyukoov.project.errors;

public class NoSuchEntityInDatabaseException extends RuntimeException{
    public NoSuchEntityInDatabaseException(String message) {
        super(message);
    }
}
