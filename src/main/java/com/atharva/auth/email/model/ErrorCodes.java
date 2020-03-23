package com.atharva.auth.email.model;

public enum ErrorCodes {
    SUCCESS(200),

    PASS_INCORRECT(401),
    ID_INCORRECT(402),
    ID_ALREADY_EXITS(403),
    AUTH_KEY_NOT_VALID(405),

    UNKNOWN(500);

    int code;
    ErrorCodes(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
