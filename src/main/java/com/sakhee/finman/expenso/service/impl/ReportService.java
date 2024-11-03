package com.sakhee.finman.expenso.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sakhee.finman.expenso.entity.User;

@Service
public class ReportService {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpenseService expenseService;

    public Map<String, BigDecimal> getIncomeExpenseSummary(User user, LocalDate startDate, LocalDate endDate) {
        BigDecimal totalIncome = incomeService.calculateTotalIncome(user, startDate, endDate);
        BigDecimal totalExpenses = expenseService.calculateTotalExpenses(user, startDate, endDate);
        
        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpenses", totalExpenses);
        
        return summary;
    }
    
    public Map<String, BigDecimal> getCategoryBreakdown(User user, LocalDate startDate, LocalDate endDate) {
        return expenseService.calculateExpensesByCategory(user, startDate, endDate);
    }
    

    public Map<String, BigDecimal> getIncomeBySource(User user, LocalDate startDate, LocalDate endDate) {
        return incomeService.categorizeIncomeBySource(user, startDate, endDate);
    }
    
}
