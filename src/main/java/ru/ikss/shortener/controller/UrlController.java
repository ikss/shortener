package ru.ikss.shortener.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.ikss.shortener.exception.NotFoundException;
import ru.ikss.shortener.model.UrlInfo;
import ru.ikss.shortener.model.UrlRequest;
import ru.ikss.shortener.model.UrlResponse;
import ru.ikss.shortener.service.ShortenerService;

@Controller
public class UrlController {
    private final ShortenerService shortenerService;
    private final String redirectUri;

    @Autowired
    public UrlController(ShortenerService shortenerService, @Value("${server.redirectUri}") String redirectUri) {
        this.shortenerService = shortenerService;
        if (!redirectUri.endsWith("/")) {
            redirectUri += "/";
        }
        this.redirectUri = redirectUri;
    }

    @GetMapping(path = "/{shortUrl}")
    public void redirect(HttpServletResponse httpServletResponse, @PathVariable String shortUrl) {
        UrlInfo info = shortenerService.getUrlInfo(shortUrl);
        if (info == null) {
            throw new NotFoundException("URL '" + shortUrl + "' not found");
        }
        shortenerService.incrementRedirectCount(info);
        httpServletResponse.setStatus(info.getRedirectType());
        httpServletResponse.setHeader(HttpHeaders.LOCATION, info.getFullUrl());
    }

    @PostMapping(value = EntryPoints.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(@Valid @RequestBody UrlRequest urlRegisterRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String accountName = auth.getName();
        String shortUrl = shortenerService.create(urlRegisterRequest.getUrl(), urlRegisterRequest.getRedirectType(), accountName);
        return new ResponseEntity<>(new UrlResponse(redirectUri + shortUrl), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = EntryPoints.STATISTIC, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> getStatistics(@NotEmpty @PathVariable String accountId) {
        return shortenerService.getStatistics(accountId);
    }
}