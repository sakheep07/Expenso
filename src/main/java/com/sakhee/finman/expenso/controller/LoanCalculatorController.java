package com.sakhee.finman.expenso.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sakhee.finman.expenso.entity.LoanEmiHistory;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.LoanEmiHistoryRepository;
import com.sakhee.finman.expenso.service.impl.LoanCalculatorService;

@Controller
public class LoanCalculatorController {

    private final LoanCalculatorService loanCalculatorService;
    private final LoanEmiHistoryRepository loanEmiHistoryRepository;

    public LoanCalculatorController(LoanCalculatorService loanCalculatorService, LoanEmiHistoryRepository loanEmiHistoryRepository) {
        this.loanCalculatorService = loanCalculatorService;
        this.loanEmiHistoryRepository = loanEmiHistoryRepository;
    }

    // Display the loan-calculator.html page
    @GetMapping("/loan-calculator")
    public String displayLoanCalculatorPage() {
        return "loan-calculator";
    }

    // API endpoint to calculate EMI, returns JSON data for AJAX
    @GetMapping("/loan-calculator/calculate-emi")
    @ResponseBody
    public Map<String, Object> calculateEMI(
            @RequestParam double principal,
            @RequestParam double annualRate,
            @RequestParam int tenureInYears,
            @AuthenticationPrincipal User user) {

        return loanCalculatorService.calculateEMIWithAmortization(principal, annualRate, tenureInYears, user);
    }
    
    @GetMapping("/loan-history")
    public String viewLoanHistory(@AuthenticationPrincipal User user, Model model) {
        List<LoanEmiHistory> history = loanEmiHistoryRepository.findByUser(user);
        model.addAttribute("history", history);
        return "loan-history";
    }

}



