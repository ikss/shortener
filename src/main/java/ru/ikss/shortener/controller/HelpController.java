package ru.ikss.shortener.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelpController {
    private final String apiUri;

    @Autowired
    public HelpController(@Value("${server.apiUri}") String apiUri) {
        this.apiUri = apiUri;
    }

    @GetMapping(value = EntryPoints.HELP)
    public String showHelp() {
        return "redirect:" + apiUri;
    }
}