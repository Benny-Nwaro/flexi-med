package com.example.flexiMed.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ADMIN, DISPATCHER, DRIVER, USER;

    @JsonCreator
    public static Role fromString(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}
