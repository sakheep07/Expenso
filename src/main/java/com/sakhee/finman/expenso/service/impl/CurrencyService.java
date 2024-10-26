package com.sakhee.finman.expenso.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sakhee.finman.expenso.dto.CurrencyResponse;

import org.springframework.web.client.HttpClientErrorException;
import java.util.HashMap;

@Service
public class CurrencyService {

    private static final String API_URL = "https://api.freecurrencyapi.com/v1/latest";
    private static final String API_KEY = "fca_live_YyGzf9X4XkhSUHaAhkGnDtLyLVyfmdYqbuBOoQ5D"; // Replace with your actual API key

    public Map<String, Double> getExchangeRates(String baseCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Double> exchangeRates = new HashMap<>();

        try {
            // Construct the full URL with API key and base currency
            String requestUrl = API_URL + "?apikey=" + API_KEY + "&base_currency=" + baseCurrency;

            // Make the API call without Authorization header
            CurrencyResponse response = restTemplate.getForObject(requestUrl, CurrencyResponse.class);

            if (response != null && response.getRates() != null) {
                exchangeRates = response.getRates();
            }
        } catch (HttpClientErrorException e) {
            // Log the error and handle it appropriately
            System.out.println("Error fetching exchange rates: " + e.getMessage());
        }

        return exchangeRates;
    }
}


