package com.crypto_trading_sim.websocket;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class KrakenWebSocketClient {
    private WebSocketClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String KRAKEN_WS_URL = "wss://ws.kraken.com/v2";
    private final ConcurrentHashMap<String, Double> prices = new ConcurrentHashMap<>();

    @PostConstruct
    public void connect() {
        try {
            client = new WebSocketClient(new URI(KRAKEN_WS_URL)) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    log.info("Connected to Kraken WebSocket API");
                    String subscribeMessage = """
                    {
                        "method": "subscribe",
                        "params": {
                            "channel": "ticker",
                            "symbol": [
                                "BTC/USD", "ETH/USD", "USDT/USD", "XRP/USD", "SOL/USD",
                                "TRX/USD", "USDC/USD", "ADA/USD", "DOGE/USD", "DOT/USD",
                                "AVAX/USD", "SHIB/USD", "MATIC/USD", "WBTC/USD", "UNI/USD",
                                "LTC/USD", "LINK/USD", "ALGO/USD", "BCH/USD", "XLM/USD"
                                ]
                        }
                    }
                    """;
                    send(subscribeMessage);
                }

                @Override
                public void onMessage(String message) {
                    try {
                        JsonNode node = objectMapper.readTree(message);
                        log.info("Message: {}", node);
                        if (node.has("channel") && node.get("channel").asText().equals("ticker") && node.has("data")) {
                            JsonNode dataArray = node.get("data");
                            if (dataArray.isArray() && dataArray.size() > 0) {
                                for (JsonNode tickerData : dataArray) {
                                    String symbol = tickerData.get("symbol").asText();
                                    double lastPrice = tickerData.get("last").asDouble();

                                    prices.put(symbol, lastPrice);
                                    log.info("Updated price: {} -> {}", symbol, lastPrice);
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error processing message:  {}", e);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("WebSocket closed. Code: {} Reason: {} Remote: {}", code, reason, remote);
                }

                @Override
                public void onError(Exception ex) {
                    log.error("WebSocket error: {}", ex);
                }
            };
            client.connect();
        } catch (Exception e) {
            log.error("Error connecting to Kraken WebSocket API: {}", e);
        }
    }

    public ConcurrentHashMap<String, Double> getPrices() {
        return prices;
    }

    public Double getLatestPrice(String symbol) {
        return prices.get(symbol);
    }
}

