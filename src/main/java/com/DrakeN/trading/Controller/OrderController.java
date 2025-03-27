package com.DrakeN.trading.Controller;


import com.DrakeN.trading.Domain.ORDER_TYPE;
import com.DrakeN.trading.Enitty.CryptoEntity;
import com.DrakeN.trading.Enitty.Order;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Response.Request.CreateOrderRequest;
import com.DrakeN.trading.Service.CrytoEntityService;
import com.DrakeN.trading.Service.OrderService;
import com.DrakeN.trading.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CrytoEntityService crytoEntityService;

//    @Autowired
//    private WalletTransactionService walletTransactionService;


    @PostMapping("/create")
    public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwt, @RequestBody CreateOrderRequest request) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        CryptoEntity coin = crytoEntityService.findById(request.getCoinId());

        Order order = orderService.processOrder(coin,request.getQuantity(),request.getOrderType(), user);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable("orderId") Long orderId, @RequestHeader("Authorization") String jwt) throws Exception {
        if(jwt == null){
            throw new Exception("Invalid token, please sign in again");
        }
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);

        if(order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        } else{
            throw new Exception("Invalid User, please sign in again");
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders(@RequestHeader("Authorization") String jwt,
                                                    @RequestParam(required = false) ORDER_TYPE orderType,
                                                    @RequestParam(required = false) String assetSymbol) throws Exception {
        if(jwt == null){
            throw new Exception("Invalid token, please sign in again");
        }
        Long userId = userService.findUserProfileByJwt(jwt).getId();
        List<Order> userOrders = orderService.getAllOrdersByUser(userId, orderType, assetSymbol);
        return ResponseEntity.ok(userOrders);
    }

}
