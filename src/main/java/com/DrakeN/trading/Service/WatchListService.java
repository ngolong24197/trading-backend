package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.CryptoEntity;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Enitty.WatchList;
import org.springframework.boot.autoconfigure.ssl.SslProperties;

public interface WatchListService {


    WatchList findUserWatchList(Long userId) throws Exception;
    WatchList createWatchList(User user);

    WatchList findById(Long id);

    CryptoEntity addItemToWatchList(CryptoEntity coin, User user) throws Exception;
}
