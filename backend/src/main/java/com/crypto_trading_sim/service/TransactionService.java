package com.crypto_trading_sim.service;

import com.crypto_trading_sim.model.Transaction;
import com.crypto_trading_sim.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionRepository.findAll();
    }

    public void recordTransaction(String type, String symbol, double amount, double price, double previousPrice) {
        double profitOrLoss = 0;
        if (type.equals("SELL")) {
            profitOrLoss = (price - previousPrice) * amount;
        }

        Transaction transaction = new Transaction(type, symbol, amount, price, profitOrLoss);
        transactionRepository.save(transaction);
    }
}
