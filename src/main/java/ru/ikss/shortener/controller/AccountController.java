package ru.ikss.shortener.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.ikss.shortener.model.AccountRequest;
import ru.ikss.shortener.model.AccountResponse;
import ru.ikss.shortener.service.AccountService;

@Controller
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = EntryPoints.ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        String password = accountService.createAccount(accountRequest.getId());
        AccountResponse response = new AccountResponse();

        if (password == null) {
            response.setDescription("Account already exists");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        response.setSuccess(true);
        response.setPassword(password);
        response.setDescription("Your account opened");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}