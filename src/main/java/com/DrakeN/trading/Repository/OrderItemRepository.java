package com.DrakeN.trading.Repository;

import com.DrakeN.trading.Enitty.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
