package com.example.minicash.library.common.response;

public class NormalResponse {
    private final boolean success;
    private final String message;

    public NormalResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
}
