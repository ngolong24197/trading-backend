package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.CryptoEntity;
import com.DrakeN.trading.Repository.CryptoEntityRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CrytoEntityServiceImpl implements CrytoEntityService {

    @Autowired
    private CryptoEntityRepository cryptoEntityRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd";



    @Override
    public List<CryptoEntity> getCoinList(int page) throws Exception {
        String url = BASE_URL + "&per_page=10&page=" + page;
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers = new HttpHeaders();
           HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String> response = restTemplate.exchange(url,org.springframework.http.HttpMethod.GET,entity,String.class);
            List<CryptoEntity> coinList = objectMapper.readValue(response.getBody(), new TypeReference<List<CryptoEntity>>() {
            });

            return coinList;


        }
        catch(HttpClientErrorException | HttpServerErrorException exception){
            throw new Exception(exception.getMessage());
        }


    }

    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String> response = restTemplate.exchange(url,org.springframework.http.HttpMethod.GET,entity,String.class);


            return response.getBody();


        }
        catch(HttpClientErrorException | HttpServerErrorException exception){
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public String getCoinDetails(String coinId) throws Exception {
    String url = "https://api.coingecko.com/api/v3/coins/" + coinId;
    RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String> response = restTemplate.exchange(url,org.springframework.http.HttpMethod.GET,entity,String.class);
            JsonNode root = objectMapper.readTree(response.getBody());

            CryptoEntity cryptoEntity = new CryptoEntity();
            cryptoEntity.setId(root.get("id").asText());
            cryptoEntity.setName(root.get("name").asText());
            cryptoEntity.setSymbol(root.get("symbol").asText());
            cryptoEntity.setImage(root.get("image").get("large").asText());

            JsonNode marketData = root.get("market_data");

            cryptoEntity.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
            cryptoEntity.setMarketCap(marketData.get("market_cap").get("usd").asLong());
            cryptoEntity.setMarketCapRank(marketData.get("market_cap_rank").asInt());
            cryptoEntity.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
            cryptoEntity.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
            cryptoEntity.setLow24h(marketData.get("low_24h").get("usd").asDouble());
                cryptoEntity.setPriceChange24h(marketData.get("price_change_24h").asDouble());
            cryptoEntity.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
            cryptoEntity.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
            cryptoEntity.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asLong());

            cryptoEntity.setTotalSupply(marketData.get("total_supply").asLong());

            cryptoEntityRepository.save(cryptoEntity);

            return response.getBody();


        }
        catch(HttpClientErrorException | HttpServerErrorException exception){
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public CryptoEntity findById(String coinId) throws Exception {
        Optional<CryptoEntity> coin = cryptoEntityRepository.findById(coinId);
        if(coin.isEmpty()) {
            throw new Exception("Coin not found");
        }
        return coin.get();
    }

    @Override
    public String searchCoin(String keyword) throws Exception {


        String url = "https://api.coingecko.com/api/v3/search?query=" + keyword ;
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String> response = restTemplate.exchange(url,org.springframework.http.HttpMethod.GET,entity,String.class);


            return response.getBody();


        }
        catch(HttpClientErrorException | HttpServerErrorException exception){
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public String get50TopCoinsByMarketCapRank() throws Exception {

        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=50&page=1";
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String> response = restTemplate.exchange(url,org.springframework.http.HttpMethod.GET,entity,String.class);


            return response.getBody();


        }
        catch(HttpClientErrorException | HttpServerErrorException exception){
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public String getTreadingCoins() throws Exception {
        String url = "https://api.coingecko.com/api/v3/search/trending";
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            ResponseEntity<String> response = restTemplate.exchange(url,org.springframework.http.HttpMethod.GET,entity,String.class);


            return response.getBody();


        }
        catch(HttpClientErrorException | HttpServerErrorException exception){
            throw new Exception(exception.getMessage());
        }
    }
}
