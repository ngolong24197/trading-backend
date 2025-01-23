package com.DrakeN.trading.Repository;

import com.DrakeN.trading.Enitty.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUserId(Long userId);



}
