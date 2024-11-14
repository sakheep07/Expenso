package com.sakhee.finman.expenso.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sakhee.finman.expenso.entity.FutureInvestmentHistory;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.service.impl.FutureInvestmentService;

@Controller
public class FutureInvestmentController {

    @Autowired
    private FutureInvestmentService futureInvestmentService;

    @GetMapping("/future-investment")
    public String showInvestmentPage() {
        return "future-investment";
    }

    @PostMapping("/calculate-sip")
    public String calculateSIP(
            @RequestParam double monthlyInvestment,
            @RequestParam int investmentPeriod,
            @RequestParam String riskLevel,
            Model model,
            @AuthenticationPrincipal User user) { // assuming a logged-in user is available

        double expectedRate = futureInvestmentService.getExpectedRate(riskLevel);
        double futureValue = futureInvestmentService.calculateSIP(monthlyInvestment, investmentPeriod, expectedRate);

        // Save history
        futureInvestmentService.saveInvestmentHistory(monthlyInvestment, investmentPeriod, riskLevel, expectedRate, futureValue, user);

        model.addAttribute("futureValue", futureValue);
        model.addAttribute("riskLevel", riskLevel);
        model.addAttribute("expectedRate", expectedRate);
        
        return "future-investment";
    }
    
    @GetMapping("/future-investment-history")
    public String viewInvestmentHistory(Model model, @AuthenticationPrincipal User user) {
        List<FutureInvestmentHistory> investmentHistory = futureInvestmentService.getInvestmentHistory(user);
        model.addAttribute("investmentHistory", investmentHistory);
        return "future-investment-history";
    }
}



