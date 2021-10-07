package com.evsyukoov.project.errors;

public class ServerTimeoutException extends RuntimeException {
    public ServerTimeoutException(String message) {
        super(message);
    }
}
