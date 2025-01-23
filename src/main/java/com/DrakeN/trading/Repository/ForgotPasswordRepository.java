package com.DrakeN.trading.Repository;

import com.DrakeN.trading.Enitty.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken,Long> {
    ForgotPasswordToken findByUserId(Long userId);

    ForgotPasswordToken findByOtpAndSendTo(String otp, String email);

}
