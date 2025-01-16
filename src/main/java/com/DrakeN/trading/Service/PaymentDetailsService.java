package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.PaymentDetails;
import com.DrakeN.trading.Enitty.User;

public interface PaymentDetailsService {

    PaymentDetails addPaymentDetails(String accountNumber, String accountHolder, String iBanNUmber,String bankName, User user);


    PaymentDetails getUserPaymentDetails(User user);


}
