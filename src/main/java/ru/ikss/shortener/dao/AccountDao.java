package ru.ikss.shortener.dao;

import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.ikss.shortener.model.AccountInfo;

@Component
public class AccountDao {
    private static final String GET_ACCOUNT = "SELECT id, password FROM accounts WHERE id = ?";
    private static final String ACCOUNT_EXISTS = "SELECT count(*) FROM accounts WHERE id = ?";
    private static final String CREATE_ACCOUNT = "INSERT INTO accounts (id, password) VALUES (?,?)";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<AccountInfo> accountInfoMapper = (rs, rowNum) -> new AccountInfo(rs.getString("id"), rs.getString("password"));

    public AccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(AccountInfo accountInfo) {
        jdbcTemplate.update(CREATE_ACCOUNT, accountInfo.getId(), accountInfo.getPassword());
    }

    public AccountInfo getById(String id) {
        List<AccountInfo> results = jdbcTemplate.query(GET_ACCOUNT, accountInfoMapper, id);
        return DataAccessUtils.uniqueResult(results);
    }

    public boolean isExists(String id) {
        Integer count = jdbcTemplate.queryForObject(ACCOUNT_EXISTS, Integer.class, id);
        return count != null && count != 0;
    }
}
