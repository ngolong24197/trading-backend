package com.DrakeN.trading.Repository;

import com.DrakeN.trading.Enitty.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentOrder,Long> {
}
