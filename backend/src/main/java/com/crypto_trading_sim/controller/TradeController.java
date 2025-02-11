package com.crypto_trading_sim.controller;

import com.crypto_trading_sim.service.AccountService;
import com.crypto_trading_sim.model.Holding;
import com.crypto_trading_sim.model.Transaction;
import com.crypto_trading_sim.websocket.KrakenWebSocketClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trade")
public class TradeController {
    private final AccountService accountService;
    private final KrakenWebSocketClient webSocketService;

    public TradeController(AccountService accountService, KrakenWebSocketClient webSocketService) {
        this.accountService = accountService;
        this.webSocketService = webSocketService;
    }

    @GetMapping("/balance")
    public double getBalance() {
        return accountService.getBalance();
    }

    @GetMapping("/holdings")
    public List<Holding> getHoldings() {
        return accountService.getHoldings();
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactionHistory() {
        return accountService.getTransactionHistory();
    }

    @PostMapping("/buy")
    public String buyCrypto(@RequestParam String symbol, @RequestParam double amount) {
        double price = webSocketService.getPrices().getOrDefault(symbol, 0.0);
        if (price == 0.0) {
            return "Error: No current price for " + symbol;
        }
        return accountService.buyCrypto(symbol, amount, price);
    }

    @PostMapping("/sell")
    public String sellCrypto(@RequestParam String symbol, @RequestParam double amount) {
        double price = webSocketService.getPrices().getOrDefault(symbol, 0.0);
        if (price == 0.0) {
            return "Error: No current price for " + symbol;
        }
        return accountService.sellCrypto(symbol, amount, price);
    }

    @PostMapping("/reset")
    public String resetAccount() {
        accountService.resetAccount();
        return "The balance is reset to $10,000!";
    }
}
