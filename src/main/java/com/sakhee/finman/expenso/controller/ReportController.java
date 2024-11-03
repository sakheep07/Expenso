package com.sakhee.finman.expenso.controller;

import com.sakhee.finman.expenso.entity.Expense;
import com.sakhee.finman.expenso.entity.Income;
import com.sakhee.finman.expenso.entity.User;
import com.sakhee.finman.expenso.repository.UserRepository;
import com.sakhee.finman.expenso.service.impl.ReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepository;

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
        Map<String, BigDecimal> incomeCategoryBreakdown = new HashMap<>();
        List<Income> incomeDetails = new ArrayList<>();
        List<Expense> expenseDetails = new ArrayList<>();

        if (startDate != null && endDate != null) {
            if (!startDate.isBefore(endDate)) {
                model.addAttribute("error", "Start date must be before end date.");
            } else {
                User user = getCurrentUser();

                summary = reportService.getIncomeExpenseSummary(user, startDate, endDate);
                categoryBreakdown = reportService.getCategoryBreakdown(user, startDate, endDate);
                incomeCategoryBreakdown = reportService.getIncomeBySource(user, startDate, endDate);
                incomeDetails = reportService.getIncomeDetails(user, startDate, endDate);
                expenseDetails = reportService.getExpenseDetails(user, startDate, endDate);

                model.addAttribute("summary", summary);
                model.addAttribute("categoryBreakdown", categoryBreakdown);
                model.addAttribute("incomeCategoryBreakdown", incomeCategoryBreakdown);
                model.addAttribute("incomeDetails", incomeDetails); // Changed here
                model.addAttribute("expenseDetails", expenseDetails);
                model.addAttribute("message", "Income and expense summary retrieved successfully.");
            }
        } else {
            model.addAttribute("message", "Please enter a date range to view the income and expense summary.");
        }

        return "reports";
    }
    
    @GetMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportToPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        User user = getCurrentUser();
        // Fetch income and expense details
        List<Income> incomeDetails = reportService.getIncomeDetails(user, startDate, endDate);
        List<Expense> expenseDetails = reportService.getExpenseDetails(user, startDate, endDate);
        
        // Generate PDF content including income and expense details
        byte[] pdfContent = reportService.generatePdfReport(user, startDate, endDate, incomeDetails, expenseDetails);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "report.pdf");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(new ByteArrayInputStream(pdfContent)));
    }


    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        byte[] excelContent = reportService.generateExcelReport(getCurrentUser(), startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "vnd.ms-excel"));
        headers.setContentDispositionFormData("attachment", "report.xlsx");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(new ByteArrayInputStream(excelContent)));
    }

    private User getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByEmail(username);
    }
}
