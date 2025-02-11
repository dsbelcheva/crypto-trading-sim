import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import CryptoPrices from './components/CryptoPrices';
import TransactionHistory from './components/TransactionHistory';
import AccountInfo from './components/AccountInfo';
import './App.css';

const App = () => {
    return (
        <Router>
         <div className="app-container">
            <nav className="navbar">
                <Link to="/" className="nav-link">Top 20 Crypto Prices</Link> |
                <Link to="/history" className="nav-link">Transaction History</Link> |
                <Link to="/account" className="nav-link">Account</Link>
            </nav>
            <Routes>
                <Route path="/" element={<CryptoPrices />} />
                <Route path="/history" element={<TransactionHistory />} />
                <Route path="/account" element={<AccountInfo />} />
            </Routes>
         </div>
        </Router>
    );
};

export default App;

