package com.sakhee.finman.expenso.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoanCalculatorService {

    public Map<String, Object> calculateEMIWithAmortization(double principal, double annualRate, int tenureInYears) {
        double monthlyRate = annualRate / (12 * 100);
        int tenureInMonths = tenureInYears * 12;

        double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, tenureInMonths)) /
                (Math.pow(1 + monthlyRate, tenureInMonths) - 1);

        double totalPayment = emi * tenureInMonths;
        double totalInterest = totalPayment - principal;

        // Generate amortization schedule
        List<Map<String, Object>> amortizationSchedule = new ArrayList<>();
        double balance = principal;

        for (int month = 1; month <= tenureInMonths; month++) {
            double interest = balance * monthlyRate;
            double principalPayment = emi - interest;
            balance -= principalPayment;

            Map<String, Object> monthDetail = new HashMap<>();
            monthDetail.put("month", month);
            monthDetail.put("emi", emi);
            monthDetail.put("interest", interest);
            monthDetail.put("principalPayment", principalPayment);
            monthDetail.put("balance", Math.max(balance, 0));

            amortizationSchedule.add(monthDetail);
        }

        return Map.of(
                "emi", emi,
                "totalPayment", totalPayment,
                "totalInterest", totalInterest,
                "amortizationSchedule", amortizationSchedule
        );
    }

    public List<Map<String, Object>> compareLoans(List<Double> principals, List<Double> annualRates, List<Integer> tenures) {
        List<Map<String, Object>> comparisonResults = new ArrayList<>();

        for (int i = 0; i < principals.size(); i++) {
            double principal = principals.get(i);
            double annualRate = annualRates.get(i);
            int tenure = tenures.get(i);

            Map<String, Object> result = calculateEMIWithAmortization(principal, annualRate, tenure);
            comparisonResults.add(result);
        }

        return comparisonResults;
    }
}

