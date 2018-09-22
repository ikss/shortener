package ru.ikss.shortener.model;

public class UrlInfo {
    private String shortUrl;
    private String fullUrl;
    private String accountId;
    private int redirectType;
    private int redirectCount;

    public UrlInfo(String shortUrl, String fullUrl, int redirectType, String accountId) {
        this(shortUrl, fullUrl, redirectType, accountId, 0);
    }

    public UrlInfo(String shortUrl, String fullUrl, int redirectType, String accountId, int redirectCount) {
        this.shortUrl = shortUrl;
        this.fullUrl = fullUrl;
        this.redirectType = redirectType;
        this.accountId = accountId;
        this.redirectCount = redirectCount;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public UrlInfo setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
        return this;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public UrlInfo setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public UrlInfo setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public int getRedirectType() {
        return redirectType;
    }

    public UrlInfo setRedirectType(int redirectType) {
        this.redirectType = redirectType;
        return this;
    }

    public int getRedirectCount() {
        return redirectCount;
    }

    public UrlInfo setRedirectCount(int redirectCount) {
        this.redirectCount = redirectCount;
        return this;
    }
}