package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.PaymentOrder;
import com.DrakeN.trading.Domain.PAYMENT_METHOD;
import com.DrakeN.trading.Domain.PAYMENT_ORDER_STATUS;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Repository.PaymentRepository;
import com.DrakeN.trading.Repository.UserRepository;
import com.DrakeN.trading.Response.Response.PaymentResponse;
import com.DrakeN.trading.Ulti.VNPayUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${vnpay.apiUrl}")
    private String VNPAY_URL;

    @Value("${vnpay.tmnCode}")
    private String VNPAY_TMNCODE;

    @Value("${vnpay.hashSecret}")
    private String VNPAY_HASHSECRET;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    @Override
    public PaymentOrder createOrder(User user, Long amount, PAYMENT_METHOD method) {
        PaymentOrder order = new PaymentOrder();
        order.setAmount(amount);
        order.setMethod(method);
        order.setUser(user);
        order.setStatus(PAYMENT_ORDER_STATUS.PENDING);
        return paymentRepository.save(order);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentRepository.findById(id).orElseThrow(() -> new Exception("Payment order not found"));
    }


    @Override
    public Boolean processPayment(PaymentOrder paymentOrder, String paymentId, HttpServletRequest request) throws StripeException {
        if (paymentOrder.getMethod().equals(PAYMENT_METHOD.VNPAY)) {
            PaymentResponse response = createVNPAYLink(paymentOrder, request);
            return response != null;
        } else if (paymentOrder.getMethod().equals(PAYMENT_METHOD.STRIPE)) {
            PaymentResponse response = createStripe(paymentOrder.getUser(), paymentOrder.getAmount(), paymentOrder.getId());
            return response != null;
        }
        return false;
    }



//    @Override
//    public PaymentResponse createVNPAYLing(PaymentOrder request, HttpServletRequest resp) {
//        try {
//            Map<String, String> params = new HashMap<>();
//
//           String url = VNPayUtil.getIpAddress(resp);
//
//            params.put("vnp_Version", "2.1.0");
//            params.put("vnp_Command", "pay");
//            params.put("vnp_TmnCode", VNPAY_TMNCODE);
//            params.put("vnp_Amount", String.valueOf(request.getAmount() * 100));
//            params.put("vnp_CurrCode", "VND");
//            params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
//            params.put("vnp_OrderInfo", "Payment for user " + request.getUser().getId());
//            params.put("vnp_ReturnUrl", vnp_ReturnUrl);
//            params.put("vnp_IpAddr", url);
//            params.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
//
//            // Build query string and signature
//            String queryString = VNPayUtil.buildQueryString(params);
//            String secureHash = VNPayUtil.hmacSHA512(VNPAY_HASHSECRET, queryString);
//
//            // Generate payment link
//            String paymentLink = VNPAY_URL + "?" + queryString + "&vnp_SecureHash=" + secureHash;
//
//            PaymentResponse response = new PaymentResponse();
//            response.setPaymentUrl(paymentLink);
//
//            return response;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error creating VNPay link: " + e.getMessage());
//        }
//    }

    @Override
    public PaymentResponse createVNPAYLink(PaymentOrder request, HttpServletRequest httpServletRequest) {
        try {
            Map<String, String> params = new HashMap<>();

            String clientIp = VNPayUtil.getIpAddress(httpServletRequest);

            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", VNPAY_TMNCODE);
            params.put("vnp_Amount", String.valueOf(request.getAmount() * 100));
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
            params.put("vnp_OrderInfo", "Payment for user " + request.getUser().getId());
            params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            params.put("vnp_IpAddr", clientIp);
            params.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

            // Build query string and signature
            String queryString = VNPayUtil.buildQueryString(params);
            String secureHash = VNPayUtil.hmacSHA512(VNPAY_HASHSECRET, queryString);

            // Generate payment link
            String paymentLink = VNPAY_URL + "?" + queryString + "&vnp_SecureHash=" + secureHash;

            PaymentResponse response = new PaymentResponse();
            response.setPaymentUrl(paymentLink);

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error creating VNPay link: " + e.getMessage(), e);
        }
    }


    @Override
    public PaymentResponse createStripe(User user, Long amount, Long orderId) throws StripeException {

            Stripe.apiKey = stripeSecretKey;

//            // Create payment intent
//            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                    .setAmount(amount * 100) // Amount in cents
//                    .setCurrency("vnd")
//                    .putMetadata("orderId", String.valueOf(orderId))
//                    .build();
//
//            PaymentIntent paymentIntent = PaymentIntent.create(params);
//
//            PaymentResponse response = new PaymentResponse();
//            response.setPaymentUrl(paymentIntent.getClientSecret());
//
//            return response;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error creating Stripe payment: " + e.getMessage());
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:5173/wallet?order_id=" + orderId)
                    .setCancelUrl("http://localhost:5173/payment/cancel")
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("vnd")
                                    .setUnitAmount(amount*100)
                                    .setProductData(SessionCreateParams
                                            .LineItem
                                            .PriceData
                                            .ProductData
                                            .builder()
                                            .setName("Top up wallet")
                                            .build())
                                    .build())
                            .build()
                    ).build();
            Session session = Session.create(params);
            System.out.println("Session created: " + session.getId());

            PaymentResponse response = new PaymentResponse();
            response.setPaymentUrl(session.getUrl());
            return response;



    }
}
