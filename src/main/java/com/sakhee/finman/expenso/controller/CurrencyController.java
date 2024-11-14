package com.sakhee.finman.expenso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.sakhee.finman.expenso.entity.CurrencyConversionHistory;
import com.sakhee.finman.expenso.service.impl.CurrencyService;

import java.util.List;
import java.util.Map;

@Controller
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/currency-converter")
    public String showConverterPage(Model model) {
        Map<String, Double> currencies = currencyService.getExchangeRates("USD");
        model.addAttribute("currencies", currencies);
        return "currency-converter";
    }

    @PostMapping("/currency-convert")
    public String convertCurrency(Model model, double amount, String fromCurrency, String toCurrency) {
        double convertedAmount = currencyService.convertCurrency(amount, fromCurrency, toCurrency);

        // Add results to the model
        model.addAttribute("convertedAmount", String.format("%.2f", convertedAmount));
        model.addAttribute("toCurrency", toCurrency);
        model.addAttribute("currencies", currencyService.getExchangeRates("USD"));

        return "currency-converter";
    }
    
    // New method to display currency conversion history
    @GetMapping("/currency-history")
    public String showConversionHistory(Model model) {
        List<CurrencyConversionHistory> history = currencyService.getConversionHistory();
        model.addAttribute("history", history);
        return "currency-history"; // A new Thymeleaf template to display history
    }
}





