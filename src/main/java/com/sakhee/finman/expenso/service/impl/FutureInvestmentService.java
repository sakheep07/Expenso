package com.sakhee.finman.expenso.service.impl;

import org.springframework.stereotype.Service;

@Service
public class FutureInvestmentService {

    // Determine the expected rate based on the user's risk level choice
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

    // Calculate SIP based on monthly investment, period, and expected rate
    public double calculateSIP(double monthlyInvestment, int investmentPeriod, double expectedRate) {
        double monthlyRate = expectedRate / 12 / 100;
        int totalMonths = investmentPeriod * 12;

        // Calculate the future value using the SIP formula
        double futureValue = monthlyInvestment * (Math.pow(1 + monthlyRate, totalMonths) - 1) / monthlyRate * (1 + monthlyRate);
        return futureValue;
    }
}


