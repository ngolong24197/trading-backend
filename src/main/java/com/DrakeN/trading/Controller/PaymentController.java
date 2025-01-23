package com.DrakeN.trading.Controller;


import com.DrakeN.trading.Domain.PAYMENT_METHOD;
import com.DrakeN.trading.Enitty.PaymentOrder;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Response.Response.PaymentResponse;
import com.DrakeN.trading.Service.PaymentService;
import com.DrakeN.trading.Service.UserService;
import com.stripe.model.PaymentMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @PostMapping("/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(@PathVariable PAYMENT_METHOD paymentMethod, @PathVariable Long amount, @RequestHeader("Authorization") String jwt, HttpServletRequest request)throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        PaymentResponse paymentResponse;

        PaymentOrder order = paymentService.createOrder(user,amount,paymentMethod);

        if(paymentMethod.equals(PAYMENT_METHOD.VNPAY)){
                paymentResponse = paymentService.createVNPAYLink(order, request);
        }
        else{
            paymentResponse = paymentService.createStripe(user, amount, order.getId());
        }
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);

    }


}
