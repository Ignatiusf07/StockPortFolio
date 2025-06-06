package com.bp.PortFolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

@Service
public class StockPriceService {
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final Map<String, PriceCache> priceCache;
    private static final long CACHE_DURATION = 5 * 60 * 1000; // 5 minutes

    public StockPriceService(@Value("${alpha.vantage.api.key}") String apiKey) {
        this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;
        this.priceCache = new HashMap<>();
    }

    public double getCurrentPrice(String symbol) {
        try {
            // Check cache first
            PriceCache cachedPrice = priceCache.get(symbol);
            if (cachedPrice != null &&
                    System.currentTimeMillis() - cachedPrice.timestamp < CACHE_DURATION) {
                return cachedPrice.price;
            }

            // Fetch new price using the new method
            String url = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("www.alphavantage.co")
                    .path("/query")
                    .queryParam("function", "GLOBAL_QUOTE")
                    .queryParam("symbol", symbol)
                    .queryParam("apikey", apiKey)
                    .build()
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            if (response == null) {
                throw new RuntimeException("Empty response from Alpha Vantage API");
            }

            JSONObject json = new JSONObject(response);
            if (!json.has("Global Quote")) {
                throw new RuntimeException("Invalid response format from Alpha Vantage API");
            }

            JSONObject globalQuote = json.getJSONObject("Global Quote");
            String priceStr = globalQuote.getString("05. price");
            double price = Double.parseDouble(priceStr);

            priceCache.put(symbol, new PriceCache(price, System.currentTimeMillis()));
            return price;
        } catch (JSONException e) {
            throw new RuntimeException("Failed to parse response from Alpha Vantage API", e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid price format received from Alpha Vantage API", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch stock price for " + symbol, e);
        }
    }

    public Map<String, Double> getCurrentPrices(String... symbols) {
        Map<String, Double> prices = new HashMap<>();
        for (String symbol : symbols) {
            prices.put(symbol, getCurrentPrice(symbol));
        }
        return prices;
    }

    private static class PriceCache {
        final double price;
        final long timestamp;

        PriceCache(double price, long timestamp) {
            this.price = price;
            this.timestamp = timestamp;
        }
    }
}