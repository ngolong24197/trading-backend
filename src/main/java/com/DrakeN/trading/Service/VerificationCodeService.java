package com.DrakeN.trading.Service;

import com.DrakeN.trading.Domain.VerificationType;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Enitty.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType type);

    VerificationCode getVerificationCodeByUser(Long userId);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    void deleteVerificationCode(VerificationCode verificationCode);

//    Boolean verifyVerificationCode(VerificationCode verificationCode, String otp);

}
