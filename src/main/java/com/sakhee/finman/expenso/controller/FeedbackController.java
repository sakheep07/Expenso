package com.sakhee.finman.expenso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeedbackController {

    @GetMapping("/feedback")
    public String feedbackExpenso() {
        return "feedback"; // return the Thymeleaf view name
    }
}

