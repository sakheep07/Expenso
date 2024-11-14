package com.sakhee.finman.expenso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about-expenso")
    public String aboutExpenso() {
        return "about-expenso"; // return the Thymeleaf view name
    }
}

