package com.DrakeN.trading.Controller;


import com.DrakeN.trading.Enitty.PaymentDetails;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Service.PaymentDetailsService;
import com.DrakeN.trading.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paymentDetails")
public class PaymentDetailsController {

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/add-detail")
    public ResponseEntity<PaymentDetails> addPaymentDetailsDetail(@RequestBody PaymentDetails paymentDetailsRequest, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails details = paymentDetailsService.addPaymentDetails(paymentDetailsRequest.getAccountNumber(), paymentDetailsRequest.getAccountHolderName(), paymentDetailsRequest.getiBanCode(), paymentDetailsRequest.getBankName(),user);

        return new ResponseEntity<>(details, HttpStatus.CREATED);


    }

    @GetMapping("/get-detail")
    public ResponseEntity<PaymentDetails> getPaymentDetailsDetail(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails details = paymentDetailsService.getUserPaymentDetails(user);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
