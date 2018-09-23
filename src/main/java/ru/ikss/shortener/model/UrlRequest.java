package ru.ikss.shortener.model;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UrlRequest {
    @NotBlank
    @JsonProperty(value = "url")
    private String url;

    @Range(min = 301, max = 302)
    @JsonProperty(value = "redirectType")
    private int redirectType = HttpStatus.FOUND.value();

    public String getUrl() {
        return url;
    }

    public UrlRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public UrlRequest setRedirectType(int redirectType) {
        this.redirectType = redirectType;
        return this;
    }
}
