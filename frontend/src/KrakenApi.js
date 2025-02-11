import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/trade';

export const getBalance = async () => {
    const response = await axios.get(`${API_BASE_URL}/balance`);
    return response.data;
};

export const getHoldings = async () => {
    const response = await axios.get(`${API_BASE_URL}/holdings`);
    return response.data;
};

export const buyCrypto = async (symbol, amount, price) => {
    const response = await axios.post(`${API_BASE_URL}/buy`, null, {
        params: { symbol, amount, price }
    });
    return response.data;
};

export const sellCrypto = async (symbol, amount, price) => {
    const response = await axios.post(`${API_BASE_URL}/sell`, null, {
        params: { symbol, amount, price }
    });
    return response.data;
};

export const getTransactionHistory = async () => {
    const response = await axios.get(`${API_BASE_URL}/transactions`);
    return response.data;
};

export const resetAccount = async () => {
    const response = await axios.post(`${API_BASE_URL}/reset`);
    return response.data;
};
