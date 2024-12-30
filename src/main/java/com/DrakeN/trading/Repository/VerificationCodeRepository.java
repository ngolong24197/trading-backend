package com.DrakeN.trading.Repository;

import com.DrakeN.trading.Enitty.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    VerificationCode findByUserId(Long userId);
}
