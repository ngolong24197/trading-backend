package com.DrakeN.trading.Controller;

import com.DrakeN.trading.Enitty.CryptoEntity;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Enitty.WatchList;
import com.DrakeN.trading.Service.CrytoEntityService;
import com.DrakeN.trading.Service.UserService;
import com.DrakeN.trading.Service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watchList")
public class WatchListController {

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private UserService userService;

    @Autowired
    private CrytoEntityService crytoEntityService;


    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchList(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        WatchList watchList = watchListService.findUserWatchList(user.getId());
        return new ResponseEntity<>(watchList, HttpStatus.OK);

    }

    @PostMapping("/create")
    public ResponseEntity<WatchList> createWatchList(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        WatchList watchList = new WatchList();
        watchList.setUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(watchList);
    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<WatchList> getWatchList(@PathVariable("watchlistId") Long watchlistId) throws Exception {
        WatchList watchList = watchListService.findById(watchlistId);
        return new ResponseEntity<>(watchList, HttpStatus.OK);

    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<CryptoEntity> addCoinToWatchList(@PathVariable("coinId") String coinId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        CryptoEntity coin = crytoEntityService.findById(coinId );

        CryptoEntity addedCoin = watchListService.addItemToWatchList(coin, user);
        return ResponseEntity.ok(addedCoin);

    }
}
