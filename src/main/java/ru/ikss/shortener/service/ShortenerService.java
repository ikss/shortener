package ru.ikss.shortener.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ru.ikss.shortener.dao.UrlDao;
import ru.ikss.shortener.exception.KnownException;
import ru.ikss.shortener.model.UrlInfo;
import ru.ikss.shortener.utils.ShortenerUtils;

@Service
public class ShortenerService {
    private final UrlDao urlDao;

    @Autowired
    public ShortenerService(UrlDao urlDao) {
        this.urlDao = urlDao;
    }

    public String create(String url, int redirectType, String account) {
        UrlInfo info = urlDao.getByFullUrlAndAccountId(url, account);
        if (info == null) {
            long nextId = urlDao.getNextId();
            info = new UrlInfo(nextId, url, redirectType, account);
            urlDao.create(info);
        } else if (info.getRedirectType() != redirectType) {
            throw new KnownException("Conflict in redirect types for", HttpStatus.CONFLICT);
        }
        return ShortenerUtils.encode(info.getId());
    }

    public UrlInfo getUrlInfo(String shortUrl) {
        return urlDao.getById(ShortenerUtils.decode(shortUrl));
    }

    public void incrementRedirectCount(long id) {
        urlDao.incrementRedirectCount(id);
    }

    public Map<String, Integer> getStatistics(String accountId) {
        return urlDao.getByAccountId(accountId).stream()
            .collect(Collectors.toMap(UrlInfo::getFullUrl, UrlInfo::getRedirectCount, Integer::sum));
    }
}
