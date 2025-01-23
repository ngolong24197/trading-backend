package com.DrakeN.trading.Service;

import com.DrakeN.trading.Domain.VerificationType;
import com.DrakeN.trading.Enitty.ForgotPasswordToken;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Repository.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service

public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    public ForgotPasswordServiceImpl( ForgotPasswordRepository forgotPasswordRepository) {
        this.forgotPasswordRepository = forgotPasswordRepository;

    }


    @Override
//    @Transactional
    public ForgotPasswordToken createToken(User user, String otp, String type, String sendTo) {
       ForgotPasswordToken token = new ForgotPasswordToken();
       token.setUser(user);
       token.setType(type);
       token.setSendTo(sendTo);
       token.setOtp(otp);
        return   forgotPasswordRepository.save(token);

    }

    @Override
    @Transactional
    public ForgotPasswordToken findById(Long id) {
        Optional<ForgotPasswordToken> forgotPasswordToken = forgotPasswordRepository.findById(id);
       return forgotPasswordToken.orElse(null);
    }

    @Override
    @Transactional
    public ForgotPasswordToken findByUserId(Long userId) {
       return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public ForgotPasswordToken findByOtpAndSendTo(String otp, String email) {
        return forgotPasswordRepository.findByOtpAndSendTo(otp,email);
    }

    @Override
    @Transactional
    public void deleteToken(ForgotPasswordToken forgotPasswordToken) {
        forgotPasswordRepository.delete(forgotPasswordToken);
    }
}
