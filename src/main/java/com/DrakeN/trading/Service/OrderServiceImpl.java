package com.DrakeN.trading.Service;

import com.DrakeN.trading.Domain.ORDER_STATUS;
import com.DrakeN.trading.Domain.ORDER_TYPE;
import com.DrakeN.trading.Enitty.*;
import com.DrakeN.trading.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CryptoEntityRepository cryptoEntityRepository;
    private final WalletRepository walletRepository;
    private final OrderItemRepository orderItemRepository;
    private final WalletService walletService;
    private final AssetService assetService;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, CryptoEntityRepository cryptoEntityRepository, WalletRepository walletRepository, OrderItemRepository orderItemRepository, WalletService walletService, AssetService assetService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cryptoEntityRepository = cryptoEntityRepository;
        this.walletRepository = walletRepository;
        this.orderItemRepository = orderItemRepository;
        this.walletService = walletService;
        this.assetService = assetService;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(String.valueOf(orderId)).orElseThrow(()-> new RuntimeException("Order not found")   );
    }

    @Override
    public Order createOrder(User user, OrderItem orderItem, ORDER_TYPE type) {
        double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();

        Order order =  new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(type);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimeStamp(LocalDateTime.now());
        order.setOrderStatus(ORDER_STATUS.PENDING);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrdersByUser(Long userId, ORDER_TYPE orderType, String assetSymbol) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Order processOrder(CryptoEntity coin, double quantity, ORDER_TYPE type, User user) throws Exception {

        if(type.equals(ORDER_TYPE.BUY)) {
            return buyAsset(coin,quantity,user);

        }
        else if(type.equals(ORDER_TYPE.SELL)) {
            return sellAsset(coin,quantity,user);

        }
        return null;
    }

    private OrderItem createOrderItem(CryptoEntity coin, double quantity, double sellPrice, double buyPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setSellPrice(sellPrice);
        orderItem.setBuyPrice(buyPrice);
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(CryptoEntity coin, double quantity, User user) throws Exception {
        if(quantity<=0){
            throw new Exception("Quantity must be greater than 0");

        }

        double buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin,quantity,0,buyPrice);
        Order order = createOrder(user,orderItem,ORDER_TYPE.BUY);
        orderItem.setOrder(order);

        walletService.payOrderPayment(order,user);
        order.setOrderStatus(ORDER_STATUS.SUCCESS);
        order.setOrderType(ORDER_TYPE.BUY);
        Order savedOrder = orderRepository.save(order);


        // create asset

        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(user.getId(),coin.getId());
        if(oldAsset==null){
            assetService.createAsset(orderItem.getCoin(),user,orderItem.getQuantity());
        }
        else{
            assetService.updateAsset(oldAsset.getId(),quantity);
        }



        return savedOrder;

    }

    @Transactional
    public Order sellAsset(CryptoEntity coin, double quantity, User user) throws Exception {


        if(quantity<=0){
            throw new Exception("Quantity must be greater than 0");

        }
        double sellPrice = coin.getCurrentPrice();



        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(),coin.getId());
        double buyPrice = assetToSell.getBuyPrice();
        if(assetToSell!=null){


            OrderItem orderItem = createOrderItem(coin,quantity,sellPrice,buyPrice);
        //OrderItem orderItem = createOrderItem(coin,quantity,sellPrice,0);
        Order order = createOrder(user,orderItem,ORDER_TYPE.SELL);
        orderItem.setOrder(order);

        if(assetToSell.getQuantity() >= quantity) {


            order.setOrderStatus(ORDER_STATUS.SUCCESS);
            order.setOrderType(ORDER_TYPE.SELL);
            Order savedOrder = orderRepository.save(order);
            walletService.payOrderPayment(order,user);
            Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), - quantity);
            if(updatedAsset.getQuantity()*coin.getCurrentPrice()<=1){
                assetService.deleteAsset(updatedAsset.getId());
            }
            return savedOrder;
        }
        throw new Exception("Insufficient coin quantity to sell");


        }
        throw  new Exception("Asset not found");





    }
}
