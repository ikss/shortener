package ru.ikss.shortener.model;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRequest {
    @NotBlank
    @JsonProperty(value = "AccountId")
    private String id;

    public String getId() {
        return id;
    }
}
