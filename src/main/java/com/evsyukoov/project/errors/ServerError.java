package com.evsyukoov.project.errors;

public class ServerError extends RuntimeException {
    public ServerError(String message) {
        super(message);
    }
}
