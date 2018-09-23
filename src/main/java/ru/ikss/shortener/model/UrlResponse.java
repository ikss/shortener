package ru.ikss.shortener.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UrlResponse implements BaseResponse {
    @JsonProperty(value = "ShortUrl")
    private String shortUrl;

    public String getShortUrl() {
        return shortUrl;
    }

    public UrlResponse setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
        return this;
    }
}
