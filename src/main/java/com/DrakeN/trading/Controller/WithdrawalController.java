package com.DrakeN.trading.Controller;


import com.DrakeN.trading.Domain.WALLET_TRANSACTION_TYPE;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Enitty.Wallet;
import com.DrakeN.trading.Enitty.Withdrawl;
import com.DrakeN.trading.Response.Request.WalletTransactionRequest;
import com.DrakeN.trading.Service.UserService;
import com.DrakeN.trading.Service.WalletService;
import com.DrakeN.trading.Service.WithdrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/withdrawal")
public class WithdrawalController {

    @Autowired
    private WithdrawlService withdrawlService;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

//    @Autowired
//    private WalletTransactionService walletTransactionService;

    @PostMapping("/{amount}")
    public ResponseEntity<?> withdrawRequest(@PathVariable Long amount, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.findWalletById(user.getId());

        Withdrawl withdrawl = withdrawlService.requestWithDrawl(amount,user);
        walletService.addBalance(wallet, -withdrawl.getAmount());

//        WalletTransaction walletTransaction = walletTransactionService.createTransaction(wallet, WALLET_TRANSACTION_TYPE.WITHDRAWAL,null,"bank account withdrawal", withdrawl.getAmount());
        return new ResponseEntity<>(withdrawl, HttpStatus.CREATED);

    }

    @PatchMapping("/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawl(@PathVariable Long id, @PathVariable boolean accept, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Withdrawl withdrawl = withdrawlService.processWithDrawl(id,accept);
        Wallet wallet = walletService.findWalletById(user.getId());

        // if the request to withdrawal is rejected then the withdrawal amount will be added back to the wallet
        if(!accept){
            walletService.addBalance(wallet, withdrawl.getAmount());
        }

        return new ResponseEntity<>(withdrawl, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Withdrawl>> getWithdrawalHistory(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawl> withdrawlList = withdrawlService.getUserWithdrawlHistory(user);
        return new ResponseEntity<>(withdrawlList, HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Withdrawl>> getAllWithdrawalRequest(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawl> withdrawlList = withdrawlService.getAllWithdrawRequest();
        return new ResponseEntity<>(withdrawlList, HttpStatus.OK);
    }



}
