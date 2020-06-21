package com.apps.rkanaje.otprelay.service;

public class RelayUser {

    private final String phone;
    private final String email;

    public RelayUser(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
