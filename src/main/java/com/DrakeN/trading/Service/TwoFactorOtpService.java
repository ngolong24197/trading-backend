package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.TwoFactorOTP;
import com.DrakeN.trading.Enitty.User;

public interface TwoFactorOtpService {

    TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);
    TwoFactorOTP findByUser(Long userId);
    TwoFactorOTP findById(String id);
    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp);
    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);
}
