package ru.ikss.shortener.model;

public class UrlInfo {
    private long id;
    private String fullUrl;
    private String accountId;
    private int redirectType;
    private int redirectCount;

    public UrlInfo(long id, String fullUrl, int redirectType, String accountId) {
        this(id, fullUrl, redirectType, accountId, 0);
    }

    public UrlInfo(long id, String fullUrl, int redirectType, String accountId, int redirectCount) {
        this.id = id;
        this.fullUrl = fullUrl;
        this.redirectType = redirectType;
        this.accountId = accountId;
        this.redirectCount = redirectCount;
    }

    public long getId() {
        return id;
    }

    public UrlInfo setShortUrl(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "UrlInfo{" +
            "id=" + id +
            ", fullUrl='" + fullUrl + '\'' +
            ", accountId='" + accountId + '\'' +
            ", redirectType=" + redirectType +
            ", redirectCount=" + redirectCount +
            '}';
    }
}