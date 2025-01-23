package com.DrakeN.trading.Service;

import com.DrakeN.trading.Domain.ORDER_TYPE;
import com.DrakeN.trading.Enitty.CryptoEntity;
import com.DrakeN.trading.Enitty.Order;
import com.DrakeN.trading.Enitty.OrderItem;
import com.DrakeN.trading.Enitty.User;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface OrderService {
    Order getOrderById(Long orderId);

    Order createOrder(User user, OrderItem orderItem, ORDER_TYPE type);

    List<Order> getAllOrdersByUser(Long userId,ORDER_TYPE orderType,String assetSymbol);

    Order processOrder(CryptoEntity coin, double quantity, ORDER_TYPE type, User user) throws Exception;
}
