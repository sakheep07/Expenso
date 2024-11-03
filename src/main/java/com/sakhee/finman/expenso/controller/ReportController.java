package com.sakhee.finman.expenso.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.UserRepository;
import com.sakhee.finman.expenso.service.impl.ReportService;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepository;

    // Method for the reports landing page
    @GetMapping
    public String reportsLandingPage(Model model) {
        model.addAttribute("message", "Please enter a date range to view the income and expense summary.");
        return "reports";
    }

    @GetMapping("/income-expense-summary")
    public String generateReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        Map<String, BigDecimal> summary = new HashMap<>();
        Map<String, BigDecimal> categoryBreakdown = new HashMap<>();
        Map<String, BigDecimal> incomeCategoryBreakdown = new HashMap<>(); // New variable for income breakdown

        if (startDate != null && endDate != null) {
            if (!startDate.isBefore(endDate)) {
                model.addAttribute("error", "Start date must be before end date.");
            } else {
                User user = getCurrentUser();
                
                // Fetch income and expense summary based on the provided date range
                summary = reportService.getIncomeExpenseSummary(user, startDate, endDate);
                
                // Fetch category breakdown for expenses
                categoryBreakdown = reportService.getCategoryBreakdown(user, startDate, endDate);
                
                // Fetch category breakdown for income (make sure this method exists in your service)
                incomeCategoryBreakdown = reportService.getIncomeBySource(user, startDate, endDate);
                
                model.addAttribute("summary", summary);
                model.addAttribute("categoryBreakdown", categoryBreakdown);
                model.addAttribute("incomeCategoryBreakdown", incomeCategoryBreakdown); // Add to model
                model.addAttribute("message", "Income and expense summary retrieved successfully.");
            }
        } else {
            model.addAttribute("message", "Please enter a date range to view the income and expense summary.");
        }

        return "reports";
    }


    private User getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByEmail(username);
    }
}


