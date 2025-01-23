package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.PaymentDetails;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Repository.PaymentDetailsRepository;
import com.DrakeN.trading.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService{


    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolder, String iBanNUmber, String bankName, User user) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);
        paymentDetails.setiBanCode(iBanNUmber);
        paymentDetails.setAccountHolderName(accountHolder);


        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }
}
