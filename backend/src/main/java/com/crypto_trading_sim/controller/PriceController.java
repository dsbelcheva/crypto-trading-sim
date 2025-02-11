package com.crypto_trading_sim.controller;

import com.crypto_trading_sim.websocket.KrakenWebSocketClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prices")
public class PriceController {
    private final KrakenWebSocketClient webSocketClient;

    public PriceController(KrakenWebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    @GetMapping
    public Map<String, Double> getPrices() {
        return webSocketClient.getPrices();
    }
}
