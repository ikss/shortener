package ru.ikss.shortener.dao;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.ikss.shortener.model.UrlInfo;

@Component
public class UrlDao {
    private static final String GET_BY_SHORT_URL = "SELECT short_url, full_url, redirect_type, account_id, redirect_count FROM urls WHERE short_url = ?";
    private static final String GET_BY_FULL_URL_AND_ACCOUNT =
        "SELECT short_url, full_url, redirect_type, account_id, redirect_count FROM urls WHERE full_url = ? AND account_id = ?";
    private static final String GET_BY_ACCOUNT_ID = "SELECT short_url, full_url, redirect_type, account_id, redirect_count FROM urls WHERE account_id = ?";
    private static final String CREATE_URL = "INSERT INTO urls (short_url, full_url, redirect_type, account_id) VALUES (?,?,?,?)";
    private static final String INCREMENT_COUNT = "UPDATE urls SET redirect_count = redirect_count + 1 WHERE short_url = ? AND account_id = ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<UrlInfo> urlInfoMapper = (rs, rowNum) ->
        new UrlInfo(rs.getString("short_url"), rs.getString("full_url"), rs.getInt("redirect_type"), rs.getString("account_id"), rs.getInt("redirect_count"));

    public UrlDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(UrlInfo info) {
        jdbcTemplate.update(CREATE_URL, info.getShortUrl(), info.getFullUrl(), info.getRedirectType(), info.getAccountId());
    }

    public List<UrlInfo> getByAccountId(String accountId) {
        return jdbcTemplate.query(GET_BY_ACCOUNT_ID, urlInfoMapper, accountId);
    }

    public UrlInfo getByFullUrlAndAccountId(String fullUrl, String account) {
        List<UrlInfo> results = jdbcTemplate.query(GET_BY_FULL_URL_AND_ACCOUNT, urlInfoMapper, fullUrl, account);
        return DataAccessUtils.uniqueResult(results);
    }

    public UrlInfo getByShortUrl(String shortUrl) {
        List<UrlInfo> results = jdbcTemplate.query(GET_BY_SHORT_URL, urlInfoMapper, shortUrl);
        return DataAccessUtils.uniqueResult(results);
    }

    public void incrementRedirectCount(UrlInfo info) {
        jdbcTemplate.update(INCREMENT_COUNT, info.getShortUrl(), info.getAccountId());
    }
}
