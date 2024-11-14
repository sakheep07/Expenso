package com.sakhee.finman.expenso.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sakhee.finman.expenso.dto.CurrencyResponse;
import com.sakhee.finman.expenso.entity.CurrencyConversionHistory;
import com.sakhee.finman.expenso.repository.CurrencyConversionHistoryRepository;

import org.springframework.web.client.HttpClientErrorException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CurrencyService {

    private static final String API_URL = "https://api.freecurrencyapi.com/v1/latest";
    private static final String API_KEY = "fca_live_YyGzf9X4XkhSUHaAhkGnDtLyLVyfmdYqbuBOoQ5D";

    @Autowired
    private CurrencyConversionHistoryRepository historyRepository;

    public Map<String, Double> getExchangeRates(String baseCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Double> exchangeRates = new HashMap<>();

        try {
            String requestUrl = API_URL + "?apikey=" + API_KEY + "&base_currency=" + baseCurrency;
            CurrencyResponse response = restTemplate.getForObject(requestUrl, CurrencyResponse.class);

            if (response != null && response.getRates() != null) {
                exchangeRates = response.getRates();
            }
        } catch (HttpClientErrorException e) {
            System.out.println("Error fetching exchange rates: " + e.getMessage());
        }

        return exchangeRates;
    }

    public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        Map<String, Double> exchangeRates = getExchangeRates("USD");
        double convertedAmount = amount * (exchangeRates.get(toCurrency) / exchangeRates.get(fromCurrency));
        
        // Save conversion history
        saveConversionHistory(amount, fromCurrency, toCurrency, convertedAmount);

        return convertedAmount;
    }


    public List<CurrencyConversionHistory> getConversionHistory() {
        return historyRepository.findAll(); // Fetch all records from the history table
    }

    private void saveConversionHistory(double amount, String fromCurrency, String toCurrency, double convertedAmount) {
        CurrencyConversionHistory history = new CurrencyConversionHistory();
        history.setAmount(amount);
        history.setFromCurrency(fromCurrency);
        history.setToCurrency(toCurrency);
        history.setConvertedAmount(convertedAmount);

        historyRepository.save(history);
    }
}



