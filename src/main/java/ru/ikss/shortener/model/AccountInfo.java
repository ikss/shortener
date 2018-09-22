package ru.ikss.shortener.model;

public class AccountInfo {
    private String id;
    private String password;

    public AccountInfo(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public AccountInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AccountInfo setPassword(String password) {
        this.password = password;
        return this;
    }
}
