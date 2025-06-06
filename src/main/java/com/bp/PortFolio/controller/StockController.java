// controller/StockController.java
package com.bp.PortFolio.controller;

import com.bp.PortFolio.model.Stock;
import com.bp.PortFolio.repository.StockRepository;
import com.bp.PortFolio.service.PortfolioService;
import com.bp.PortFolio.service.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/")
public class StockController {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private StockPriceService stockPriceService;

    private static final int PAGE_SIZE = 10;

    @GetMapping
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/loading")
    public String loading() {
        return "loading";
    }

    @GetMapping("/dashboard")
    public String index(Model model) {
        try {
            Page<Stock> stocksPage = stockRepository
                    .findAll(PageRequest.of(0, PAGE_SIZE, Sort.by("purchaseDate").descending()));
            model.addAttribute("stocks", stocksPage.getContent());
            model.addAttribute("totalValue", portfolioService.calculateTotalPortfolioValue());
            model.addAttribute("totalShares", portfolioService.calculateTotalShares());
            model.addAttribute("totalGainLoss", portfolioService.calculateTotalGainLoss());
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load portfolio data. Please try again later.");
            return "error";
        }
    }

    @GetMapping("/api/stocks")
    @ResponseBody
    public ResponseEntity<Page<Stock>> getStocks(@RequestParam(defaultValue = "0") int page) {
        try {
            Page<Stock> stocksPage = stockRepository
                    .findAll(PageRequest.of(page, PAGE_SIZE, Sort.by("purchaseDate").descending()));
            return ResponseEntity.ok(stocksPage);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/stocks")
    public String createStock(@ModelAttribute Stock stock) {
        try {
            if (stock.getTicker() == null || stock.getTicker().trim().isEmpty()) {
                return "redirect:/?error=Stock ticker is required";
            }
            if (stock.getQuantity() <= 0) {
                return "redirect:/?error=Quantity must be greater than 0";
            }
            if (stock.getPurchasePrice() <= 0) {
                return "redirect:/?error=Purchase price must be greater than 0";
            }

            if (stock.getPurchaseDate() == null) {
                stock.setPurchaseDate(LocalDate.now());
            }

            try {
                stock.setCurrentPrice(stockPriceService.getCurrentPrice(stock.getTicker()));
            } catch (Exception e) {
                return "redirect:/?error=Failed to fetch current price: " + e.getMessage();
            }

            stockRepository.save(stock);
            return "redirect:/?success=Stock added successfully";
        } catch (Exception e) {
            return "redirect:/?error=Failed to add stock: " + e.getMessage();
        }
    }

    @GetMapping("/api/stocks/{id}")
    @ResponseBody
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        try {
            return stockRepository.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @RequestMapping(value = "/api/stocks/{id}", method = { RequestMethod.POST, RequestMethod.PUT })
    public String updateStock(@PathVariable Long id, @ModelAttribute Stock stockDetails) {
        try {
            if (stockDetails.getTicker() == null || stockDetails.getTicker().trim().isEmpty()) {
                return "redirect:/?error=Stock ticker is required";
            }
            if (stockDetails.getQuantity() <= 0) {
                return "redirect:/?error=Quantity must be greater than 0";
            }
            if (stockDetails.getPurchasePrice() <= 0) {
                return "redirect:/?error=Purchase price must be greater than 0";
            }

            return stockRepository.findById(id)
                    .map(stock -> {
                        try {
                            // Update all fields
                            stock.setTicker(stockDetails.getTicker());
                            stock.setQuantity(stockDetails.getQuantity());
                            stock.setPurchasePrice(stockDetails.getPurchasePrice());

                            // Handle purchase date
                            if (stockDetails.getPurchaseDate() != null) {
                                stock.setPurchaseDate(stockDetails.getPurchaseDate());
                            }

                            // Handle notes
                            if (stockDetails.getNotes() != null) {
                                stock.setNotes(stockDetails.getNotes());
                            }

                            // Update current price
                            try {
                                stock.setCurrentPrice(stockPriceService.getCurrentPrice(stock.getTicker()));
                            } catch (Exception e) {
                                // If price fetch fails, keep the existing price
                                System.err.println("Failed to fetch current price: " + e.getMessage());
                            }

                            stockRepository.save(stock);
                            return "redirect:/?success=Stock updated successfully";
                        } catch (Exception e) {
                            return "redirect:/?error=Failed to update stock: " + e.getMessage();
                        }
                    })
                    .orElse("redirect:/?error=Stock not found");
        } catch (Exception e) {
            return "redirect:/?error=Failed to update stock: " + e.getMessage();
        }
    }

    @DeleteMapping("/api/stocks/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteStock(@PathVariable Long id) {
        try {
            return stockRepository.findById(id)
                    .map(stock -> {
                        try {
                            stockRepository.delete(stock);
                            stockRepository.flush();
                            return ResponseEntity.ok().body(Map.of(
                                    "success", true,
                                    "message", "Stock deleted successfully"));
                        } catch (Exception e) {
                            if (e.getCause() != null && e.getCause().getMessage().contains("locked")) {
                                return ResponseEntity.status(423).body(Map.of(
                                        "success", false,
                                        "message", "Database is locked. Please try again in a few seconds."));
                            }
                            return ResponseEntity.status(500).body(Map.of(
                                    "success", false,
                                    "message", "Failed to delete stock: " + e.getMessage()));
                        }
                    })
                    .orElse(ResponseEntity.status(404).body(Map.of(
                            "success", false,
                            "message", "Stock not found")));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Failed to delete stock: " + e.getMessage()));
        }
    }

    @GetMapping("/api/stocks/portfolio/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPortfolioStats() {
        try {
            return ResponseEntity.ok(Map.of(
                    "totalValue", portfolioService.calculateTotalPortfolioValue(),
                    "totalShares", portfolioService.calculateTotalShares(),
                    "totalGainLoss", portfolioService.calculateTotalGainLoss()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/test/price/{symbol}")
    @ResponseBody
    public ResponseEntity<Double> testStockPrice(@PathVariable String symbol) {
        try {
            double price = stockPriceService.getCurrentPrice(symbol);
            return ResponseEntity.ok(price);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(0.0);
        }
    }
}
