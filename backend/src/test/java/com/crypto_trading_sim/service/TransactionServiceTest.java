package com.crypto_trading_sim.service;

import com.crypto_trading_sim.model.Transaction;
import com.crypto_trading_sim.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void testGetTransactionHistory() {
        Transaction transaction1 = new Transaction("BUY", "BTC", 1.0, 50000.0, 0.0);
        Transaction transaction2 = new Transaction("SELL", "ETH", 2.0, 3000.0, 500.0);
        List<Transaction> mockTransactions = Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findAll()).thenReturn(mockTransactions);

        List<Transaction> result = transactionService.getTransactionHistory();

        assertEquals(2, result.size());
        assertEquals("BTC", result.get(0).getSymbol());
        assertEquals("ETH", result.get(1).getSymbol());

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    public void testRecordTransaction_Buy() {
        transactionService.recordTransaction("BUY", "BTC", 1.0, 50000.0, 0.0);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        assertEquals("BUY", savedTransaction.getType());
        assertEquals("BTC", savedTransaction.getSymbol());
        assertEquals(1.0, savedTransaction.getAmount());
        assertEquals(50000.0, savedTransaction.getPrice());
        assertEquals(0.0, savedTransaction.getProfitOrLoss());
    }

    @Test
    public void testRecordTransaction_Sell_WithProfit() {
        transactionService.recordTransaction("SELL", "BTC", 1.0, 55000.0, 50000.0);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        assertEquals("SELL", savedTransaction.getType());
        assertEquals("BTC", savedTransaction.getSymbol());
        assertEquals(1.0, savedTransaction.getAmount());
        assertEquals(55000.0, savedTransaction.getPrice());
        assertEquals(5000.0, savedTransaction.getProfitOrLoss());
    }

    @Test
    public void testRecordTransaction_Sell_WithLoss() {
        transactionService.recordTransaction("SELL", "BTC", 1.0, 45000.0, 50000.0);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        assertEquals("SELL", savedTransaction.getType());
        assertEquals("BTC", savedTransaction.getSymbol());
        assertEquals(1.0, savedTransaction.getAmount());
        assertEquals(45000.0, savedTransaction.getPrice());
        assertEquals(-5000.0, savedTransaction.getProfitOrLoss());
    }
}
