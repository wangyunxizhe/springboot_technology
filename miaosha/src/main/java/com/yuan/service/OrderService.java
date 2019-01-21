package com.yuan.service;

import com.yuan.error.MyException;
import com.yuan.service.model.OrderModel;

public interface OrderService {

    //创建订单
    OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws MyException;

}
