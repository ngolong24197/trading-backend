package com.DrakeN.trading.Service;

import com.DrakeN.trading.Domain.WITHDRAWL_STATUS;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Enitty.Withdrawl;
import com.DrakeN.trading.Repository.WithdrawlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class WithdrawlServiceImpl implements WithdrawlService {

    @Autowired
    private WithdrawlRepository withdrawlRepository;

    @Override
    public Withdrawl requestWithDrawl(Long amount, User user) {
        Withdrawl withdrawl = new Withdrawl();
        withdrawl.setAmount(amount);
        withdrawl.setUser(user);
        withdrawl.setStatus(WITHDRAWL_STATUS.PENDING);

        return withdrawlRepository.save(withdrawl);
    }

    @Override
    public Withdrawl processWithDrawl(Long withdrawlId, boolean accept) throws Exception {
        Optional<Withdrawl> withdrawl = withdrawlRepository.findById(withdrawlId);
        if(withdrawl.isEmpty()) {
            throw new Exception("Can't find the withdrawl request!");
        }

            Withdrawl processedWithdrawl = withdrawl.get();
            processedWithdrawl.setDate(LocalDateTime.now());
            if(accept) {
                processedWithdrawl.setStatus(WITHDRAWL_STATUS.SUCCESS);
            }
            else{

                processedWithdrawl.setStatus(WITHDRAWL_STATUS.DECLINED);
            }

        return withdrawlRepository.save(processedWithdrawl);
    }

    @Override
    public List<Withdrawl> getUserWithdrawlHistory(User user) {
        return withdrawlRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawl> getAllWithdrawRequest() {
        return withdrawlRepository.findAll();
    }
}
