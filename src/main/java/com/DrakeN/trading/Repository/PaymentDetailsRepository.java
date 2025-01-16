package com.DrakeN.trading.Repository;

import com.DrakeN.trading.Enitty.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsRepository  extends JpaRepository<PaymentDetails, Long> {

    PaymentDetails findByUserId(Long userId);
}
