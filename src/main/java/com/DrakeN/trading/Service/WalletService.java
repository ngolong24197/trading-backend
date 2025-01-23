package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.Order;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Enitty.Wallet;

import java.util.List;

public interface WalletService {

    Wallet getUserWallet(User user);

    Wallet addBalance(Wallet wallet, Long amount);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransaction(User sender, Wallet receiverWallet, Long amount) throws Exception;
    Wallet payOrderPayment(Order order, User user) throws Exception;
    List<Wallet> getAllWallets();

}
