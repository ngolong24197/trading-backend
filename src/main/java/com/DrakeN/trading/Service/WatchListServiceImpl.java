package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.CryptoEntity;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Enitty.WatchList;
import com.DrakeN.trading.Repository.CryptoEntityRepository;
import com.DrakeN.trading.Repository.UserRepository;
import com.DrakeN.trading.Repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WatchListRepository watchListRepository;

    @Autowired
    private CryptoEntityRepository cryptoEntityRepository;

    @Override
    public WatchList findUserWatchList(Long userId) throws Exception {
        WatchList watchList =  watchListRepository.findByUserId(userId);
        if (watchList == null) {
            throw new Exception("WatchList not found");
        }
        return watchList;
    }

    @Override
    public WatchList createWatchList(User user) {
        WatchList watchList = new WatchList();
        watchList.setUser(user);

        return watchListRepository.save(watchList);
    }

    @Override
    public WatchList findById(Long id) {
        return watchListRepository.findById(id).orElseThrow(()->new RuntimeException("WatchList Not Found"));
    }

    @Override
    public CryptoEntity addItemToWatchList(CryptoEntity coin, User user) throws Exception {
        WatchList watchList = findUserWatchList(user.getId());
        if(watchList.getCoinsList().contains(coin)) {
            watchList.getCoinsList().remove(coin);
        }
        else{
            watchList.getCoinsList().add(coin);
        }
        watchListRepository.save(watchList);
        return coin;
    }
}
