package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.PaymentOrder;
import com.DrakeN.trading.Domain.PAYMENT_METHOD;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Response.Response.PaymentResponse;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentOrder createOrder(User user, Long amount, PAYMENT_METHOD method);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean processPayment(PaymentOrder paymentOrder, String paymentId, HttpServletRequest request) throws Exception;

    PaymentResponse createVNPAYLink(PaymentOrder paymentOrder, HttpServletRequest req);

    PaymentResponse createStripe(User user, Long amount, Long orderId) throws StripeException;
}
