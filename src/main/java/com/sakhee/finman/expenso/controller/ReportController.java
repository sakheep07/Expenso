package com.sakhee.finman.expenso.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import java.util.HashMap;

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
        return "reports"; // Ensure you have a reports.html template for this
    }

    @GetMapping("/income-expense-summary")
    public String generateReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        
        Map<String, BigDecimal> summary = new HashMap<>();

        // Validate that startDate and endDate are provided
        if (startDate != null && endDate != null) {
            if (!startDate.isBefore(endDate)) {
                model.addAttribute("error", "Start date must be before end date.");
            } else {
                // Get the current logged-in user
                String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
                User user = userRepository.findByEmail(username);
                
                // Fetch income and expense summary based on the provided date range
                summary = reportService.getIncomeExpenseSummary(user, startDate, endDate);
                model.addAttribute("summary", summary);
                model.addAttribute("message", "Income and expense summary retrieved successfully.");
            }
        } else {
            model.addAttribute("message", "Please enter a date range to view the income and expense summary.");
        }

        // Add the summary map to the model
        model.addAttribute("summary", summary);
        
        return "reports"; // The Thymeleaf template to display the results
    }
}
