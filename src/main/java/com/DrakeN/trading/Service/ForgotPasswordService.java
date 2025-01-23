package com.DrakeN.trading.Service;

import com.DrakeN.trading.Domain.VerificationType;
import com.DrakeN.trading.Enitty.ForgotPasswordToken;
import com.DrakeN.trading.Enitty.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordService {


    ForgotPasswordToken createToken(User user, String otp, String type, String sendTo);
    ForgotPasswordToken findById(Long id);
    ForgotPasswordToken findByUserId(Long userId);

    ForgotPasswordToken findByOtpAndSendTo(String otp, String email);

    void deleteToken(ForgotPasswordToken forgotPasswordToken);

}
