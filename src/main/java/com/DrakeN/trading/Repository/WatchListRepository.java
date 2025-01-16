package com.DrakeN.trading.Repository;

import com.DrakeN.trading.Enitty.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {


    WatchList findByUserId(Long userId);


}
