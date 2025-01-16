package com.DrakeN.trading.Controller;

import com.DrakeN.trading.Enitty.CryptoEntity;
import com.DrakeN.trading.Service.CrytoEntityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crypto")
public class CryptoEntityController {

    private final CrytoEntityService cryptoEntityService;

    private final ObjectMapper objectMapper;

    public CryptoEntityController(CrytoEntityService cryptoEntityService, ObjectMapper objectMapper) {
        this.cryptoEntityService = cryptoEntityService;
        this.objectMapper = objectMapper;
    }

    @GetMapping()
    public ResponseEntity<List<CryptoEntity>> getCoinList(@RequestParam(name="page", required = false) int page) throws Exception {
        List<CryptoEntity> coins = cryptoEntityService.getCoinList(page);
        return new ResponseEntity<>(coins, HttpStatus.ACCEPTED);

    }

    @GetMapping("/{coinId}/chart")
    public ResponseEntity<JsonNode> getCoinMarketChart(@RequestParam("days") int days, @PathVariable("coinId") String coinId) throws Exception {
        String chart = cryptoEntityService.getMarketChart(coinId, days);
        JsonNode jsonNode = objectMapper.readTree(chart);


        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchCoin(@RequestParam("keyword") String keyword) throws Exception {
        String chart = cryptoEntityService.searchCoin(keyword);
        JsonNode jsonNode = objectMapper.readTree(chart);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/top50")
    public ResponseEntity<JsonNode> getTop50CoinsByMarketCapRank() throws Exception {
        String coins = cryptoEntityService.get50TopCoinsByMarketCapRank();
        JsonNode jsonNode = objectMapper.readTree(coins);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/treading")
    public ResponseEntity<JsonNode> getTreadingCoins() throws Exception {
        String coins = cryptoEntityService.getTreadingCoins();
        JsonNode jsonNode = objectMapper.readTree(coins);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/details/{coinId}")
    public ResponseEntity<JsonNode> getCoinDetails(@PathVariable("coinId") String coinId) throws Exception {
        String coins = cryptoEntityService.getCoinDetails(coinId);
        JsonNode jsonNode = objectMapper.readTree(coins);
        return ResponseEntity.ok(jsonNode);
    }






}
