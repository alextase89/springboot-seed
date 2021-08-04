package com.cubans.awssqs.api;

public enum UserRolEnum {

    ADMIN, CLIENT;

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
