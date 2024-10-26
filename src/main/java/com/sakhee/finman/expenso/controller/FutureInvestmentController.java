package com.sakhee.finman.expenso.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sakhee.finman.expenso.service.impl.FutureInvestmentService;

@Controller
public class FutureInvestmentController {

    @Autowired
    private FutureInvestmentService futureInvestmentService;

    // Display the Future Investment page
    @GetMapping("/future-investment")
    public String showInvestmentPage() {
        return "future-investment";
    }

    // Handle SIP calculation requests
    @PostMapping("/calculate-sip")
    public String calculateSIP(
            @RequestParam double monthlyInvestment,
            @RequestParam int investmentPeriod,
            @RequestParam String riskLevel,
            Model model) {
        
        // Determine the expected rate based on the selected risk level
        double expectedRate = futureInvestmentService.getExpectedRate(riskLevel);

        // Calculate the future value using the selected rate
        double futureValue = futureInvestmentService.calculateSIP(monthlyInvestment, investmentPeriod, expectedRate);
        
        model.addAttribute("futureValue", futureValue);
        model.addAttribute("riskLevel", riskLevel);
        model.addAttribute("expectedRate", expectedRate);
        return "future-investment";
    }
}


