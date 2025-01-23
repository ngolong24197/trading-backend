package com.DrakeN.trading.Repository;

import com.DrakeN.trading.Enitty.Withdrawl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawlRepository extends JpaRepository<Withdrawl,Long> {

    List<Withdrawl> findByUserId(long userId);

}
