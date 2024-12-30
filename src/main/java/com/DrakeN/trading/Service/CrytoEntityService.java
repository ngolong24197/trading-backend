package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.CryptoEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CrytoEntityService {

    List<CryptoEntity> getCoinList(int page) throws Exception;

    String getMarketChart(String coinId, int days) throws Exception;

    String getCoinDetails(String coinId) throws Exception;
    CryptoEntity findById(String coinId) throws Exception;

    String searchCoin(String keyword) throws Exception;

    String get50TopCoinsByMarketCapRank() throws Exception;
    String getTreadingCoins() throws Exception;

}
