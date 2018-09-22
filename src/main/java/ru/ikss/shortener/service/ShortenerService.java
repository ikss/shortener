package ru.ikss.shortener.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ru.ikss.shortener.dao.UrlDao;
import ru.ikss.shortener.exception.KnownException;
import ru.ikss.shortener.model.UrlInfo;

@Service
public class ShortenerService {
    private final UrlDao urlDao;

    @Autowired
    public ShortenerService(UrlDao urlDao) {
        this.urlDao = urlDao;
    }

    public String create(String url, int redirectType, String account) {
        String sanitizedUrl = sanitize(url);
        UrlInfo info = urlDao.getByFullUrlAndAccountId(sanitizedUrl, account);
        if (info == null) {
            info = new UrlInfo(shorten(sanitizedUrl), sanitizedUrl, redirectType, account);
            urlDao.create(info);
        } else if (info.getRedirectType() != redirectType) {
            throw new KnownException("Conflict in redirect types for", HttpStatus.CONFLICT);
        }
        return info.getShortUrl();
    }

    public UrlInfo getUrlInfo(String shortUrl) {
        return urlDao.getByShortUrl(shortUrl);
    }

    private String sanitize(String url) {
        return UriUtil.hasScheme(url) ? url : "http://" + url;
    }

    private String shorten(String url) {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public void incrementRedirectCount(UrlInfo info) {
        urlDao.incrementRedirectCount(info);
    }

    public Map<String, Integer> getStatistics(String accountId) {
        return urlDao.getByAccountId(accountId).stream().collect(Collectors.toMap(UrlInfo::getFullUrl, UrlInfo::getRedirectCount));
    }
}
