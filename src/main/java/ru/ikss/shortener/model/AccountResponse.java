package ru.ikss.shortener.model;

public class AccountResponse implements BaseResponse {
    private boolean success;
    private String description;
    private String password;

    public AccountResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public AccountResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public AccountResponse setPassword(String password) {
        this.password = password;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getDescription() {
        return description;
    }

    public String getPassword() {
        return password;
    }
}
