package com.DrakeN.trading.Response.Request;

import com.DrakeN.trading.Domain.ORDER_TYPE;
import lombok.Data;

@Data
public class CreateOrderRequest {

    private String coinId;
    private double quantity;
    private ORDER_TYPE orderType;

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public ORDER_TYPE getOrderType() {
        return orderType;
    }

    public void setOrderType(ORDER_TYPE orderType) {
        this.orderType = orderType;
    }
}
