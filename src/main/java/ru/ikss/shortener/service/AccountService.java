package ru.ikss.shortener.service;

import java.util.Collections;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ru.ikss.shortener.dao.AccountDao;
import ru.ikss.shortener.model.AccountInfo;

@Service
public class AccountService implements UserDetailsService {
    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);
    private final AccountDao accountDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountDao accountDao, PasswordEncoder passwordEncoder) {
        this.accountDao = accountDao;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        AccountInfo accountInfo = accountDao.getById(accountId);
        if (accountInfo == null) {
            LOG.warn("Account '{}' not found", accountId);
            throw new UsernameNotFoundException("Account with id " + accountId + " not found");
        }
        return new User(accountId, accountInfo.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }

    public String createAccount(String accountId) {
        if (accountDao.isExists(accountId)) {
            LOG.debug("Account with id '{}' already exists", accountId);
            return null;
        }
        String password = RandomStringUtils.randomAlphanumeric(8);
        AccountInfo accountInfo = new AccountInfo(accountId, passwordEncoder.encode(password));
        LOG.debug("Create new account with id '{}'", accountId);
        accountDao.create(accountInfo);
        return password;
    }

    public boolean isExists(String id) {
        return accountDao.isExists(id);
    }
}
