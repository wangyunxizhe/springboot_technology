package com.yuan.service.model;

import java.math.BigDecimal;

/**
 * 用户下单的交易模型
 */
public class OrderModel {

    private String id;

    private Integer userId;//用户id

    private Integer itemId;//商品id

    private Integer promoId;//若非空，则表示是以秒杀商品方式下单的

    private BigDecimal itemPrice;//购买商品的单价；若promoId非空，则表示是以秒杀商品价格购买的

    private Integer amount;//购买数量

    private BigDecimal orderPrice;//购买总金额

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
