package ru.ikss.shortener.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ru.ikss.shortener.exception.BadRequestException;
import ru.ikss.shortener.exception.NotFoundException;
import ru.ikss.shortener.model.UrlInfo;
import ru.ikss.shortener.model.UrlRequest;
import ru.ikss.shortener.model.UrlResponse;
import ru.ikss.shortener.service.ShortenerService;

@Controller
public class UrlController {
    private final ShortenerService shortenerService;
    private final String redirectUri;
    private final UrlValidator urlValidator;

    @Autowired
    public UrlController(ShortenerService shortenerService, @Value("${server.redirectUri}") String redirectUri) {
        this.shortenerService = shortenerService;
        if (!redirectUri.endsWith("/")) {
            redirectUri += "/";
        }
        this.redirectUri = redirectUri;
        urlValidator = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES | UrlValidator.ALLOW_2_SLASHES | UrlValidator.ALLOW_LOCAL_URLS);
    }

    @GetMapping(path = "/{shortUrl}")
    public void redirect(HttpServletResponse httpServletResponse, @PathVariable String shortUrl) {
        UrlInfo info = shortenerService.getUrlInfo(shortUrl);
        if (info == null) {
            throw new NotFoundException("URL '" + shortUrl + "' not found");
        }
        shortenerService.incrementRedirectCount(info.getId());
        httpServletResponse.setStatus(info.getRedirectType());
        httpServletResponse.setHeader(HttpHeaders.LOCATION, info.getFullUrl());
    }

    @PostMapping(value = EntryPoints.REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity register(Authentication authentication, @Valid @RequestBody UrlRequest urlRegisterRequest) {
        String shortUrl = shortenerService.create(sanitize(urlRegisterRequest.getUrl()), urlRegisterRequest.getRedirectType(), authentication.getName());
        return new ResponseEntity<>(new UrlResponse().setShortUrl(redirectUri + shortUrl), HttpStatus.CREATED);
    }

    private String sanitize(String url) {
        String urlWithScheme = UriUtil.hasScheme(url) ? url : "http://" + url;
        if (!urlValidator.isValid(urlWithScheme)) {
            throw new BadRequestException("URL " + url + " is invalid");
        }
        return urlWithScheme;
    }
}