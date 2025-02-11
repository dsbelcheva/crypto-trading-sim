package com.crypto_trading_sim.service;

import com.crypto_trading_sim.model.Account;
import com.crypto_trading_sim.model.Holding;
import com.crypto_trading_sim.model.Transaction;
import com.crypto_trading_sim.repository.AccountRepository;
import com.crypto_trading_sim.repository.HoldingRepository;
import com.crypto_trading_sim.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private HoldingRepository holdingRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetBalance_WhenAccountExists() {
        Account account = new Account(5000.0);
        when(accountRepository.findFirstByOrderByIdAsc()).thenReturn(account);

        double balance = accountService.getBalance();

        assertEquals(5000.0, balance);
        verify(accountRepository, times(1)).findFirstByOrderByIdAsc();
    }

    @Test
    public void testGetBalance_WhenAccountDoesNotExist() {
        when(accountRepository.findFirstByOrderByIdAsc()).thenReturn(null);

        double balance = accountService.getBalance();

        assertEquals(10000.0, balance);
        verify(accountRepository, times(1)).findFirstByOrderByIdAsc();
    }

    @Test
    public void testBuyCrypto_SuccessfulPurchase() {
        Account account = new Account(10000.0);
        when(accountRepository.findFirstByOrderByIdAsc()).thenReturn(account);

        Holding holding = new Holding("BTC", 1.0);
        when(holdingRepository.findBySymbol("BTC")).thenReturn(holding);

        String result = accountService.buyCrypto("BTC", 1.0, 5000.0);

        assertEquals("Bought 1.0 BTC for $5000.0", result);
        assertEquals(5000.0, account.getBalance());
        assertEquals(2.0, holding.getAmount());
        verify(accountRepository, times(1)).save(account);
        verify(holdingRepository, times(1)).save(holding);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testBuyCrypto_InsufficientBalance() {
        Account account = new Account(1000.0);
        when(accountRepository.findFirstByOrderByIdAsc()).thenReturn(account);

        String result = accountService.buyCrypto("BTC", 1.0, 5000.0);

        assertEquals("Not enough balance!", result);
        assertEquals(1000.0, account.getBalance());
        verify(accountRepository, never()).save(account);
        verify(holdingRepository, never()).save(any(Holding.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testSellCrypto_SuccessfulSale() {
        Holding holding = new Holding("BTC", 2.0);
        when(holdingRepository.findBySymbol("BTC")).thenReturn(holding);

        Account account = new Account(1000.0);
        when(accountRepository.findFirstByOrderByIdAsc()).thenReturn(account);

        String result = accountService.sellCrypto("BTC", 1.0, 5000.0);

        assertEquals("Sold 1.0 BTC for $5000.0", result);
        assertEquals(6000.0, account.getBalance());
        assertEquals(1.0, holding.getAmount());
        verify(accountRepository, times(1)).save(account);
        verify(holdingRepository, times(1)).save(holding);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testSellCrypto_InsufficientQuantity() {
        Holding holding = new Holding("BTC", 0.5);
        when(holdingRepository.findBySymbol("BTC")).thenReturn(holding);

        String result = accountService.sellCrypto("BTC", 1.0, 5000.0);

        assertEquals("Not enough quantity of BTC!", result);
        verify(accountRepository, never()).save(any(Account.class));
        verify(holdingRepository, never()).save(any(Holding.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testGetHoldings() {
        Holding holding = new Holding("BTC", 2.0);
        when(holdingRepository.findAll()).thenReturn(Collections.singletonList(holding));

        List<Holding> holdings = accountService.getHoldings();

        assertEquals(1, holdings.size());
        assertEquals("BTC", holdings.get(0).getSymbol());
        assertEquals(2.0, holdings.get(0).getAmount());
        verify(holdingRepository, times(1)).findAll();
    }

    @Test
    public void testGetTransactionHistory() {
        Transaction transaction = new Transaction("BUY", "BTC", 1.0, 5000.0, 5000.0);
        when(transactionRepository.findAllByOrderByTimestampDesc()).thenReturn(Collections.singletonList(transaction));

        List<Transaction> transactions = accountService.getTransactionHistory();

        assertEquals(1, transactions.size());
        assertEquals("BUY", transactions.get(0).getType());
        assertEquals("BTC", transactions.get(0).getSymbol());
        verify(transactionRepository, times(1)).findAllByOrderByTimestampDesc();
    }

    @Test
    public void testResetAccount() {
        accountService.resetAccount();

        verify(accountRepository, times(1)).deleteAll();
        verify(holdingRepository, times(1)).deleteAll();
        verify(transactionRepository, times(1)).deleteAll();
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}

