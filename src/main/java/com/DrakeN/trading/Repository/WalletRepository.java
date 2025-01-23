package com.DrakeN.trading.Repository;

import com.DrakeN.trading.Enitty.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository  extends JpaRepository<Wallet, Long> {

    Wallet findWalletByUserId(long userid);

}
