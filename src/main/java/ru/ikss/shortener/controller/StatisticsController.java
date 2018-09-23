package ru.ikss.shortener.controller;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.ikss.shortener.exception.BadRequestException;
import ru.ikss.shortener.service.AccountService;
import ru.ikss.shortener.service.ShortenerService;

@Controller
public class StatisticsController {
    private final ShortenerService shortenerService;
    private final AccountService accountService;

    @Autowired
    public StatisticsController(ShortenerService shortenerService, AccountService accountService) {
        this.shortenerService = shortenerService;
        this.accountService = accountService;
    }

    @ResponseBody
    @GetMapping(value = EntryPoints.STATISTIC, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> getStatistics(@NotEmpty @PathVariable String accountId) {
        if (!accountService.isExists(accountId)) {
            throw new BadRequestException("Account " + accountId + "doesn't exists");
        }
        return shortenerService.getStatistics(accountId);
    }
}