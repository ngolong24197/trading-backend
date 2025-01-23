package com.DrakeN.trading.Controller;

import com.DrakeN.trading.Enitty.Order;
import com.DrakeN.trading.Enitty.PaymentOrder;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Enitty.Wallet;
import com.DrakeN.trading.Response.Request.WalletTransactionRequest;
import com.DrakeN.trading.Response.Response.PaymentResponse;
import com.DrakeN.trading.Service.OrderService;
import com.DrakeN.trading.Service.PaymentService;
import com.DrakeN.trading.Service.UserService;
import com.DrakeN.trading.Service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;
    private final OrderService orderService;

    private final PaymentService paymentService;


    public WalletController(WalletService walletService, UserService userService, OrderService orderService, PaymentService paymentService) {
        this.walletService = walletService;
        this.userService = userService;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping()
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{receiveWalletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader("Authorization") String jwt, @PathVariable Long receiveWalletId, @RequestBody WalletTransactionRequest request) throws Exception {

        User sender = userService.findUserProfileByJwt(jwt);
        Wallet receiveWallet = walletService.findWalletById(receiveWalletId);
        Wallet wallet = walletService.walletToWalletTransaction(sender,receiveWallet, request.getAmount());


        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);



    }

    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);

        Wallet wallet = walletService.payOrderPayment(order, user);


        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
     }

    @PutMapping("/deposit")
    public ResponseEntity<Wallet> walletDeposit(@RequestHeader("Authorization") String jwt, @RequestParam(name="order_id") Long orderId,@RequestParam(name = "payment_id") String paymentId, HttpServletRequest request) throws Exception {


        User user = userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);
        if(wallet.getBalance() == null){
            wallet.setBalance(BigDecimal.valueOf(0));
        }
        PaymentOrder paymentOrder = paymentService.getPaymentOrderById(orderId);
        Boolean status = paymentService.processPayment(paymentOrder,paymentId, request);
        PaymentResponse response = new PaymentResponse();


        if(status){
            wallet=walletService.addBalance(wallet,paymentOrder.getAmount());
        }


        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Wallet>> getAllUserWallet() throws Exception {
        List<Wallet> wallets = walletService.getAllWallets();
        return new ResponseEntity<>(wallets, HttpStatus.ACCEPTED);
    }
}
