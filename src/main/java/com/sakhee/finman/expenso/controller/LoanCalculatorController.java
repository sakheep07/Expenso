package com.sakhee.finman.expenso.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sakhee.finman.expenso.service.impl.LoanCalculatorService;

@Controller
@RequestMapping("/loan-calculator")
public class LoanCalculatorController {

    private final LoanCalculatorService loanCalculatorService;

    public LoanCalculatorController(LoanCalculatorService loanCalculatorService) {
        this.loanCalculatorService = loanCalculatorService;
    }

    // Display the loan-calculator.html page
    @GetMapping
    public String displayLoanCalculatorPage() {
        // Add any necessary default data to the model if needed
        return "loan-calculator";
    }

    // API endpoint to calculate EMI, returns JSON data for AJAX
    @GetMapping("/calculate-emi")
    @ResponseBody
    public Map<String, Object> calculateEMI(
            @RequestParam double principal,
            @RequestParam double annualRate,
            @RequestParam int tenureInYears) {

        return loanCalculatorService.calculateEMIWithAmortization(principal, annualRate, tenureInYears);
    }

    // API endpoint to compare loans, returns JSON data for AJAX
    @GetMapping("/compare-loans")
    @ResponseBody
    public List<Map<String, Object>> compareLoans(
            @RequestParam List<Double> principals,
            @RequestParam List<Double> annualRates,
            @RequestParam List<Integer> tenures) {

        return loanCalculatorService.compareLoans(principals, annualRates, tenures);
    }
}


