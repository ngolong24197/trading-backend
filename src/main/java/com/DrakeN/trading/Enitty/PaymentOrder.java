package com.DrakeN.trading.Enitty;


import com.DrakeN.trading.Domain.PAYMENT_METHOD;
import com.DrakeN.trading.Domain.PAYMENT_ORDER_STATUS;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PAYMENT_ORDER_STATUS status;

    private PAYMENT_METHOD method;

    private String paymentId;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public PAYMENT_ORDER_STATUS getStatus() {
        return status;
    }

    public void setStatus(PAYMENT_ORDER_STATUS status) {
        this.status = status;
    }

    public PAYMENT_METHOD getMethod() {
        return method;
    }

    public void setMethod(PAYMENT_METHOD method) {
        this.method = method;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
