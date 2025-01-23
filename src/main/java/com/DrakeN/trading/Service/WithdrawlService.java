package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Enitty.Withdrawl;

import java.util.List;

public interface WithdrawlService {

    Withdrawl requestWithDrawl(Long amount, User user);

    Withdrawl processWithDrawl(Long withdrawlId, boolean accept) throws Exception;

    List<Withdrawl> getUserWithdrawlHistory(User user);

    List<Withdrawl> getAllWithdrawRequest();
}
