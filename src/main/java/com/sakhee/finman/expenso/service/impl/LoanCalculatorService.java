package com.sakhee.finman.expenso.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.sakhee.finman.expenso.entity.LoanEmiHistory;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.LoanEmiHistoryRepository;

@Service
public class LoanCalculatorService {

    private final LoanEmiHistoryRepository historyRepository;

    public LoanCalculatorService(LoanEmiHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public Map<String, Object> calculateEMIWithAmortization(double principal, double annualRate, int tenureInYears, User user) {
        // EMI calculation logic remains unchanged
        double monthlyRate = annualRate / (12 * 100);
        int tenureInMonths = tenureInYears * 12;
        double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, tenureInMonths)) /
                (Math.pow(1 + monthlyRate, tenureInMonths) - 1);

        double totalPayment = emi * tenureInMonths;
        double totalInterest = totalPayment - principal;

        // Save history entry
        LoanEmiHistory history = new LoanEmiHistory();
        history.setPrincipal(principal);
        history.setAnnualRate(annualRate);
        history.setTenureInYears(tenureInYears);
        history.setEmi(emi);
        history.setTotalPayment(totalPayment);
        history.setTotalInterest(totalInterest);
        history.setUser(user);
        historyRepository.save(history);

        // Return result map
        return Map.of(
                "emi", emi,
                "totalPayment", totalPayment,
                "totalInterest", totalInterest
        );
    }
}
