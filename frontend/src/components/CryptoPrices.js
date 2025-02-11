import React, { useEffect, useState } from 'react';
import axios from 'axios';
import styles from './CryptoPrices.module.css';
import { buyCrypto, sellCrypto, getBalance } from '../KrakenApi';
import { FaBitcoin, FaEthereum, FaDollarSign, FaDog, FaCoins, FaLink, FaBtc, FaShoppingCart, FaMoneyBillWave, FaWallet } from 'react-icons/fa';
import { SiBinance, SiSolana, SiLitecoin, SiPolkadot, SiAlgorand } from 'react-icons/si'

const cryptoData = {
    "BTC/USD": { name: "Bitcoin", icon: <FaBitcoin color="#f7931a" /> },
    "ETH/USD": { name: "Ethereum", icon: <FaEthereum color="#3c3cdb" /> },
    "USDT/USD": { name: "Tether", icon: <FaDollarSign color="#26a17b" /> },
    "XRP/USD": { name: "XRP", icon: <FaDollarSign color="#346aa9" /> },
    "SOL/USD": { name: "Solana", icon: <SiSolana color="#f3ba2f" /> },
    "BNB/USD": { name: "Binance Coin", icon: <SiBinance color="#f0b90b" /> },
    "USDC/USD": { name: "USD Coin", icon: <FaDollarSign color="#2775ca" /> },
    "TRX/USD": { name: "Tron", icon: <FaCoins color="#3cc8c8" /> },
    "DOGE/USD": { name: "Dogecoin", icon: <FaDog color="#c2a633" /> },
    "DOT/USD": { name: "Polkadot", icon: <SiPolkadot color="#e6007a" /> },
    "AVAX/USD": { name: "Avalanche", icon: <FaDollarSign color="#e84142" /> },
    "SHIB/USD": { name: "Shiba Inu", icon: <FaDollarSign color="#f60" /> },
    "MATIC/USD": { name: "Polygon", icon: <FaDog color="#8c52ff" /> },
    "WBTC/USD": { name: "WBitcoin", icon: <FaDollarSign color="#f0b90b" /> },
    "UNI/USD": { name: "Uniswap", icon: <FaDollarSign color="#ff007a" /> },
    "LTC/USD": { name: "Litecoin", icon: <SiLitecoin color="#b0b0b0" /> },
    "LINK/USD": { name: "Chainlink", icon: <FaLink color="#2a5ada" /> },
    "ALGO/USD": { name: "Algorand", icon: <SiAlgorand color="#000" /> },
    "BCH/USD": { name: "Bitcoin Cash", icon: <FaBtc color="#4caf50" /> },
    "XLM/USD": { name: "Stellar", icon: <FaCoins color="#08b5e5" /> },
};

const CryptoPrices = () => {
    const [prices, setPrices] = useState({});
    const [balance, setBalance] = useState(0);
    const [selectedCrypto, setSelectedCrypto] = useState("BTC/USD");
    const [amount, setAmount] = useState("");
    const [message, setMessage] = useState("");

    useEffect(() => {
        const fetchPricesAndBalance = async () => {
            try {
                const [priceResponse, balanceResponse] = await Promise.all([
                    axios.get("http://localhost:8080/api/prices"),
                    getBalance(),
                ]);

                setPrices(priceResponse.data);
                setBalance(balanceResponse);
            } catch (error) {
                console.error("Error to fetch the data:", error);
            }
        };

        fetchPricesAndBalance();
        const interval = setInterval(fetchPricesAndBalance, 5000);
        return () => clearInterval(interval);
    }, []);

    const validateInput = () => {
            if (!selectedCrypto) {
                setMessage("Please choose cryptocurrency.");
                return false;
            }
            if (!amount || amount <= 0) {
                setMessage("Please input valid quantity.");
                return false;
            }
            return true;
    };

    const handleBuy = async () => {
        if (!selectedCrypto || !prices[selectedCrypto]) return;
        const purchaseAmount = parseFloat(amount) || 0;
        if (!validateInput()) return;

        try {
            const price = prices[selectedCrypto];
            const response = await buyCrypto(selectedCrypto, purchaseAmount, price);

            if (response.includes("error")) {
                setMessage(`Error to purchase: ${response}`);
            } else {
                setMessage(response);
                const updatedBalance = await getBalance();
                setBalance(updatedBalance);
            }
        } catch (error) {
            setMessage("Failed to purchase! Check the API!");
            console.error("Error to purchase:", error);
        }
    };

    const handleSell = async () => {
        if (!selectedCrypto || !prices[selectedCrypto]) return;
        const sellAmount = parseFloat(amount) || 0;
        if (!validateInput()) return;

        try {
            const price = prices[selectedCrypto];
            const response = await sellCrypto(selectedCrypto, sellAmount, price);

            if (response.includes("error")) {
                setMessage(`Error to sell: ${response}`);
            } else {
                setMessage(response);
                const updatedBalance = await getBalance();
                setBalance(updatedBalance);
            }
        } catch (error) {
            setMessage("Failed to sell! Check the API!");
            console.error("Error to sell:", error);
        }
    };

    return (
        <div className={styles["prices-container"]}>
            <h2>Realtime Prices</h2>
            <h3><FaWallet /> Balance: ${balance.toFixed(2)}</h3>
            <div className={styles["crypto-list"]}>
                {Object.entries(prices).map(([symbol, price]) => (
                    <div key={symbol} className={styles["crypto-card"]}>
                        <div className={styles["icon"]}>{cryptoData[symbol]?.icon || <FaCoins />}</div>
                        <h3>{cryptoData[symbol]?.name || symbol}</h3>
                        <p className={styles["symbol"]}>{symbol}</p>
                        <p className={styles["price"]}>${price.toFixed(2)}</p>
                    </div>
                ))}
            </div>

            <div className={styles["trade-container"]}>
                <h3>Trade</h3>
                <select value={selectedCrypto} onChange={(e) => setSelectedCrypto(e.target.value)}>
                    {Object.keys(prices).map((crypto) => (<option key={crypto} value={crypto}>{crypto}</option>))}
                </select>
                <input
                 type="number"
                 placeholder="Quantity"
                 value={amount}
                 onChange={(e) => setAmount(e.target.value)}
                />
                <div className={styles["trade-buttons"]}>
                    <button className={styles["buy-btn"]} onClick={handleBuy}>
                        <FaShoppingCart /> Buy
                    </button>
                    <button className={styles["sell-btn"]} onClick={handleSell}>
                        <FaMoneyBillWave /> Sell
                    </button>
                </div>
                {message && <div className={styles["message"]}>{message}</div>}
            </div>
        </div>
    );
};

export default CryptoPrices;
