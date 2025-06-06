package com.bp.PortFolio.service;

import com.bp.PortFolio.model.Stock;
import com.bp.PortFolio.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PortfolioService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockPriceService stockPriceService;

    public double calculateTotalPortfolioValue() {
        List<Stock> stocks = stockRepository.findAll();
        return stocks.stream()
                .mapToDouble(Stock::getTotalValue)
                .sum();
    }

    public int calculateTotalShares() {
        List<Stock> stocks = stockRepository.findAll();
        return stocks.stream()
                .mapToInt(Stock::getQuantity)
                .sum();
    }

    public double calculateTotalGainLoss() {
        List<Stock> stocks = stockRepository.findAll();
        return stocks.stream()
                .mapToDouble(Stock::getGainLoss)
                .sum();
    }

    @Scheduled(fixedRate = 300000) // Update every 5 minutes
    public void updateStockPrices() {
        List<Stock> stocks = stockRepository.findAll();
        String[] symbols = stocks.stream()
                .map(Stock::getTicker)
                .toArray(String[]::new);

        Map<String, Double> currentPrices = stockPriceService.getCurrentPrices(symbols);

        stocks.forEach(stock -> {
            Double currentPrice = currentPrices.get(stock.getTicker());
            if (currentPrice != null) {
                stock.setCurrentPrice(currentPrice);
                stockRepository.save(stock);
            }
        });
    }
}