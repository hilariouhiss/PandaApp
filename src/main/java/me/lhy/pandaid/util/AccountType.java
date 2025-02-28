package me.lhy.pandaid.util;

import lombok.Getter;

@Getter
public enum AccountType {
    USER("USER"),
    ADMIN("ADMIN"),
    DEVELOPER("DEVELOPER");

    private final String type;

    AccountType(String type) {
        this.type = type;
    }
}
