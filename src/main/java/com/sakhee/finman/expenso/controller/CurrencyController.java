package com.sakhee.finman.expenso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.sakhee.finman.expenso.service.impl.CurrencyService;

import java.util.Map;

@Controller
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/currency-converter")
    public String showConverterPage(Model model) {
        Map<String, Double> currencies = currencyService.getExchangeRates("USD"); // Use default base currency
        model.addAttribute("currencies", currencies);
        return "currency-converter"; // Thymeleaf template name
    }

    @PostMapping("/currency-convert")
    public String convertCurrency(Model model, double amount, String fromCurrency, String toCurrency) {
        // Retrieve the exchange rates again
        Map<String, Double> currencies = currencyService.getExchangeRates("USD"); // Get rates based on a default base currency

        // Conversion logic: calculate convertedAmount based on the rates
        double convertedAmount = amount * (currencies.get(toCurrency) / currencies.get(fromCurrency));
        
        // Add results to the model
        model.addAttribute("convertedAmount", String.format("%.2f", convertedAmount)); // Format to 2 decimal places
        model.addAttribute("toCurrency", toCurrency);
        model.addAttribute("currencies", currencies); // Pass currencies again for the dropdowns

        return "currency-converter"; // Return to the same page to display the result
    }
}




