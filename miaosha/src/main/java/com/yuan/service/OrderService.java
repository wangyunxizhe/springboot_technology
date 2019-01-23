package com.yuan.service;

import com.yuan.error.MyException;
import com.yuan.service.model.OrderModel;

public interface OrderService {

    //创建订单
    //通过前端传来的promoId，校验promoId是否属于对应的itemId，并且判断该itemId的秒杀活动是否已经开始
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws MyException;

}
