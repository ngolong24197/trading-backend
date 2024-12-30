package com.DrakeN.trading.Service;

import com.DrakeN.trading.Domain.VerificationType;
import com.DrakeN.trading.Enitty.ForgotPasswordToken;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Repository.ForgotPasswordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final ForgotPasswordRepository forgotPasswordRepository;

    public ForgotPasswordServiceImpl( ForgotPasswordRepository forgotPasswordRepository) {
        this.forgotPasswordRepository = forgotPasswordRepository;

    }


    @Override
    public ForgotPasswordToken createToken(User user, String id, String otp, VerificationType type, String sendTo) {
       ForgotPasswordToken token = new ForgotPasswordToken();
       token.setUser(user);
       token.setType(type);
       token.setSendTo(sendTo);
       token.setOtp(otp);
       token.setId(id);
       return forgotPasswordRepository.save(token);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> forgotPasswordToken = forgotPasswordRepository.findById(id);
       return forgotPasswordToken.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUserId(Long userId) {
       return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken forgotPasswordToken) {
        forgotPasswordRepository.delete(forgotPasswordToken);
    }
}
