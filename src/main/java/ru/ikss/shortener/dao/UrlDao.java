package ru.ikss.shortener.dao;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.ikss.shortener.model.UrlInfo;

@Component
public class UrlDao {
    private static final String GET_NEXT_ID = "SELECT NEXTVAL('urls_sequence')";
    private static final String GET_BY_ID = "SELECT id, full_url, redirect_type, account_id, redirect_count FROM urls WHERE id = ?";
    private static final String GET_BY_FULL_URL_AND_ACCOUNT = "SELECT id, full_url, redirect_type, account_id, redirect_count FROM urls WHERE full_url = ? AND account_id = ?";
    private static final String GET_BY_ACCOUNT_ID = "SELECT id, full_url, redirect_type, account_id, redirect_count FROM urls WHERE account_id = ?";
    private static final String CREATE_URL = "INSERT INTO urls (id, full_url, redirect_type, account_id) VALUES (?,?,?,?)";
    private static final String INCREMENT_COUNT = "UPDATE urls SET redirect_count = redirect_count + 1 WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<UrlInfo> urlInfoMapper = (rs, rowNum) ->
        new UrlInfo(rs.getInt("id"), rs.getString("full_url"), rs.getInt("redirect_type"), rs.getString("account_id"), rs.getInt("redirect_count"));

    public UrlDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(UrlInfo info) {
        jdbcTemplate.update(CREATE_URL, info.getId(), info.getFullUrl(), info.getRedirectType(), info.getAccountId());
    }

    public Long getNextId() {
        return jdbcTemplate.queryForObject(GET_NEXT_ID, Long.class);
    }

    public List<UrlInfo> getByAccountId(String accountId) {
        return jdbcTemplate.query(GET_BY_ACCOUNT_ID, urlInfoMapper, accountId);
    }

    public UrlInfo getByFullUrlAndAccountId(String fullUrl, String account) {
        List<UrlInfo> results = jdbcTemplate.query(GET_BY_FULL_URL_AND_ACCOUNT, urlInfoMapper, fullUrl, account);
        return DataAccessUtils.uniqueResult(results);
    }

    public UrlInfo getById(long id) {
        List<UrlInfo> results = jdbcTemplate.query(GET_BY_ID, urlInfoMapper, id);
        return DataAccessUtils.uniqueResult(results);
    }

    public void incrementRedirectCount(long id) {
        jdbcTemplate.update(INCREMENT_COUNT, id);
    }
}
