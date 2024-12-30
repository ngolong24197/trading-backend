package com.DrakeN.trading.Service;


import com.DrakeN.trading.Domain.VerificationType;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Enitty.VerificationCode;
import com.DrakeN.trading.Repository.VerificationCodeRepository;
import com.DrakeN.trading.Ulti.otpUltis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{


    private final VerificationCodeRepository verificationCodeRepo;

    public VerificationCodeServiceImpl(VerificationCodeRepository verificationCodeRepo) {
        this.verificationCodeRepo = verificationCodeRepo;
    }

    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType type) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otpUltis.generateOtp());
        verificationCode.setType(type);
        verificationCode.setUser(user);
        return verificationCodeRepo.save(verificationCode);
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {

        return verificationCodeRepo.findByUserId(userId);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
            Optional<VerificationCode> verificationCode = verificationCodeRepo.findById(id);
            if(verificationCode.isPresent()) {
                return verificationCode.get();
            }
            throw new Exception("Verification Code not found");


    }

    @Override
    public void deleteVerificationCode(VerificationCode verificationCode) {
        verificationCodeRepo.delete(verificationCode);

    }

//    @Override
//    public Boolean verifyVerificationCode(VerificationCode verificationCode, String otp) {
//
//        return verificationCode.getOtp().equals(otp);
//    }
}
