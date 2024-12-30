package com.DrakeN.trading.Service;

import com.DrakeN.trading.Domain.VerificationType;
import com.DrakeN.trading.Enitty.ForgotPasswordToken;
import com.DrakeN.trading.Enitty.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordService {


    ForgotPasswordToken createToken(User user, String id, String otp, VerificationType type, String sendTo);
    ForgotPasswordToken findById(String id);
    ForgotPasswordToken findByUserId(Long userId);

    void deleteToken(ForgotPasswordToken forgotPasswordToken);

}
