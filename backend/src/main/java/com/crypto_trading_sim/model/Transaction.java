package com.crypto_trading_sim.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // "BUY" или "SELL"
    private String symbol;
    private double amount;
    private double price;
    private Double profitOrLoss;
    private LocalDateTime timestamp;

    public Transaction() {}

    public Transaction(String type, String symbol, double amount, double price, Double profitOrLoss) {
        this.type = type;
        this.symbol = symbol;
        this.amount = amount;
        this.price = price;
        this.profitOrLoss = profitOrLoss;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getProfitOrLoss() {
        return profitOrLoss;
    }
}
