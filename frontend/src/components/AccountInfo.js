import React, { useEffect, useState } from 'react';
import { getBalance, getHoldings, resetAccount } from '../KrakenApi';
import styles from './AccountInfo.module.css';
import { FaTimes, FaUndo, FaUser } from "react-icons/fa";

const AccountInfo = () => {
    const [balance, setBalance] = useState(0);
    const [holdings, setHoldings] = useState([]);
    const [message, setMessage] = useState('');

    useEffect(() => {
        fetchAccountData();
    }, []);

    const fetchAccountData = async () => {
        const balanceData = await getBalance();
        const holdingsData = await getHoldings();
        setBalance(balanceData);
        setHoldings(holdingsData);
    };

    const handleReset = async () => {
        const response = await resetAccount();
        setMessage(response);
        fetchAccountData();
    };

    return (
        <div className={styles["account-container"]}>
            <h2><FaUser /> User Account</h2>
            <p><strong>Balance:</strong> ${balance.toFixed(2)}</p>
            <p><strong>User:</strong> Crypto Trader</p>

            <h3>Active Holdings</h3>
            {holdings.length > 0 ? (
                <table className={styles["holdings-table"]}>
                    <thead>
                        <tr>
                            <th>Cryptocurrency</th>
                            <th>Quantity</th>
                        </tr>
                    </thead>
                    <tbody>
                        {holdings.map((holding) => (
                            <tr key={holding.symbol}>
                                <td>{holding.symbol}</td>
                                <td>{holding.amount.toFixed(4)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            ) : (
                <p><FaTimes />No active holdings.</p>
            )}

            <button className={styles["reset-btn"]} onClick={handleReset}><FaUndo /> Reset the account</button>

            {message && <p className={styles.message}>{message}</p>}
        </div>
    );
};

export default AccountInfo;
