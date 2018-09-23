package ru.ikss.shortener.controller;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.ikss.shortener.service.ShortenerService;

@Controller
public class StatisticsController {
    private final ShortenerService shortenerService;

    @Autowired
    public StatisticsController(ShortenerService shortenerService) {
        this.shortenerService = shortenerService;
    }

    @ResponseBody
    @GetMapping(value = EntryPoints.STATISTIC, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> getStatistics(@NotEmpty @PathVariable String accountId) {
        return shortenerService.getStatistics(accountId);
    }
}