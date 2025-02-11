package com.crypto_trading_sim.service;

import org.springframework.stereotype.Service;
import com.crypto_trading_sim.model.Account;
import com.crypto_trading_sim.model.Holding;
import com.crypto_trading_sim.model.Transaction;
import com.crypto_trading_sim.repository.AccountRepository;
import com.crypto_trading_sim.repository.HoldingRepository;
import com.crypto_trading_sim.repository.TransactionRepository;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final HoldingRepository holdingRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, HoldingRepository holdingRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.holdingRepository = holdingRepository;
        this.transactionRepository = transactionRepository;
    }

    public double getBalance() {
        Account account = accountRepository.findFirstByOrderByIdAsc();
        return (account != null) ? account.getBalance() : 10000.0;
    }

    public String buyCrypto(String symbol, double amount, double price) {
        Account account = accountRepository.findFirstByOrderByIdAsc();
        if (account == null) {
            account = new Account(10000.0);
        }

        double totalCost = amount * price;
        if (totalCost > account.getBalance()) {
            return "Not enough balance!";
        }

        account.setBalance(account.getBalance() - totalCost);
        accountRepository.save(account);

        Holding holding = holdingRepository.findBySymbol(symbol);
        if (holding == null) {
            holding = new Holding(symbol, 0);
        }
        holding.setAmount(holding.getAmount() + amount);
        holdingRepository.save(holding);

        saveTransaction("BUY", symbol, amount, price, totalCost);

        return "Bought " + amount + " " + symbol + " for $" + totalCost;
    }

    public String sellCrypto(String symbol, double amount, double price) {
        Holding holding = holdingRepository.findBySymbol(symbol);
        if (holding == null || holding.getAmount() < amount) {
            return "Not enough quantity of " + symbol + "!";
        }

        double totalEarnings = amount * price;

        Account account = accountRepository.findFirstByOrderByIdAsc();
        if (account == null) {
            account = new Account(10000.0);
        }
        account.setBalance(account.getBalance() + totalEarnings);
        accountRepository.save(account);

        holding.setAmount(holding.getAmount() - amount);
        if (holding.getAmount() <= 0) {
            holdingRepository.delete(holding);
        } else {
            holdingRepository.save(holding);
        }

        saveTransaction("SELL", symbol, amount, price, totalEarnings);

        return "Sold " + amount + " " + symbol + " for $" + totalEarnings;
    }

    public List<Holding> getHoldings() {
        return holdingRepository.findAll();
    }

    private void saveTransaction(String type, String symbol, double amount, double price, double total) {
        Transaction transaction = new Transaction(type, symbol, amount, price, total);
        transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionHistory() {
        return transactionRepository.findAllByOrderByTimestampDesc();
    }

    public void resetAccount() {
        accountRepository.deleteAll();
        holdingRepository.deleteAll();
        transactionRepository.deleteAll();
        accountRepository.save(new Account(10000.0));
    }
}
