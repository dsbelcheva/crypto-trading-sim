import React, { useEffect, useState } from 'react';
import { getTransactionHistory } from '../KrakenApi';
import styles from './TransactionHistory.module.css';
import { FaTimes, FaArrowUp, FaArrowDown } from "react-icons/fa";

const TransactionHistory = () => {
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        const fetchTransactions = async () => {
            const data = await getTransactionHistory();
            setTransactions(data);
        };

        fetchTransactions();
    }, []);

    return (
        <div className={styles["history-container"]}>
            <h2>Transaction History</h2>
            <table className={styles["history-table"]}>
                <thead>
                    <tr>
                        <th>Data</th>
                        <th>Type of purchase</th>
                        <th>Quantity</th>
                        <th>Crypto</th>
                        <th>Price</th>
                        <th>Profit/Loss</th>
                    </tr>
                </thead>
                <tbody>
                    {transactions.length > 0 ? (
                        transactions.map((tx) => (
                            <tr key={tx.id} className={tx.type === "SELL" && tx.profitOrLoss > 0 ? styles["profit"] : tx.type === "SELL" && tx.profitOrLoss < 0 ? styles["loss"] : ""}>
                                <td>{new Date(tx.timestamp).toLocaleString()}</td>
                                <td className={tx.type === "BUY" ? styles.success : styles.error}>
                                    {tx.type}
                                </td>
                                <td>{tx.amount}</td>
                                <td>{tx.symbol}</td>
                                <td>${tx.price.toFixed(2)}</td>
                                <td>
                                    {tx.type === "SELL" ? (
                                        tx.profitOrLoss > 0 ? (
                                            <span className={styles["profit-text"]}><FaArrowUp /> +${tx.profitOrLoss.toFixed(2)}</span>
                                        ) : (
                                            <span className={styles["loss-text"]}><FaArrowDown /> -${Math.abs(tx.profitOrLoss).toFixed(2)}</span>
                                        )
                                    ) : (
                                        "-"
                                    )}
                                </td>
                             </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="5"><FaTimes /> No transaction history.</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default TransactionHistory;
