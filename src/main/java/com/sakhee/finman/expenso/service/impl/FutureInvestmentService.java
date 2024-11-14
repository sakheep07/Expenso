package com.sakhee.finman.expenso.service.impl;

import org.springframework.stereotype.Service;

import com.sakhee.finman.expenso.entity.FutureInvestmentHistory;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.FutureInvestmentHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FutureInvestmentService {

    @Autowired
    private FutureInvestmentHistoryRepository historyRepository;

    public double getExpectedRate(String riskLevel) {
        switch (riskLevel) {
            case "smallCap":
                return 35.0;
            case "midCap":
                return 31.0;
            case "largeCap":
                return 17.0;
            default:
                throw new IllegalArgumentException("Invalid risk level");
        }
    }

    public double calculateSIP(double monthlyInvestment, int investmentPeriod, double expectedRate) {
        double monthlyRate = expectedRate / 12 / 100;
        int totalMonths = investmentPeriod * 12;

        double futureValue = monthlyInvestment * (Math.pow(1 + monthlyRate, totalMonths) - 1) / monthlyRate * (1 + monthlyRate);
        
        return futureValue;
    }

    public void saveInvestmentHistory(double monthlyInvestment, int investmentPeriod, String riskLevel, double expectedRate, double futureValue, User user) {
        FutureInvestmentHistory history = new FutureInvestmentHistory();
        history.setMonthlyInvestment(monthlyInvestment);
        history.setInvestmentPeriod(investmentPeriod);
        history.setRiskLevel(riskLevel);
        history.setExpectedRate(expectedRate);
        history.setFutureValue(futureValue);
        history.setUser(user);
        history.setTimestamp(LocalDateTime.now());
        
        historyRepository.save(history);
    }
    
    public List<FutureInvestmentHistory> getInvestmentHistory(User user) {
        return historyRepository.findByUser(user);
    }

}



