package com.DrakeN.trading.Service;

import com.DrakeN.trading.Domain.ORDER_TYPE;
import com.DrakeN.trading.Enitty.Order;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Enitty.Wallet;
import com.DrakeN.trading.Repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class WalletServiceImpl implements WalletService {



    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet =  walletRepository.findWalletByUserId(user.getId());

        if(wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            walletRepository.save(wallet);


        }

        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long amount) {

        BigDecimal balance = wallet.getBalance();
        balance = balance.add(BigDecimal.valueOf(amount));
        wallet.setBalance(balance);

        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if(wallet.isPresent()) {
            return wallet.get();
        }
        throw new Exception("Wallet Not found");

    }

    @Override
    public Wallet walletToWalletTransaction(User sender, Wallet receiverWallet, Long amount) throws Exception {
        Wallet senderWallet = walletRepository.findWalletByUserId(sender.getId());
        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new Exception("Insufficient balance");
        }
        else{
            senderWallet.setBalance(senderWallet.getBalance().subtract(BigDecimal.valueOf(amount)));
            receiverWallet.setBalance(receiverWallet.getBalance().add(BigDecimal.valueOf(amount)));
            walletRepository.save(senderWallet);
            walletRepository.save(receiverWallet);
            return senderWallet;
        }


    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        Wallet wallet = getUserWallet(user);
//        BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
        if(order.getOrderType().equals(ORDER_TYPE.BUY)){
//            if(newBalance.getBalance().compareTo(order.getPrice())<0){
           if(wallet.getBalance().compareTo(order.getPrice())<0){
               throw new Exception("Insufficient balance for order");
           }
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
           wallet.setBalance(newBalance);
        }

        else if(order.getOrderType().equals(ORDER_TYPE.SELL)){
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);

        }
        walletRepository.save(wallet);

        return wallet;
    }

    @Override
    public List<Wallet> getAllWallets() {
        List<Wallet> wallets = walletRepository.findAll();
        return wallets;
    }
}
