package com.yuan.service.impl;

import com.yuan.dao.OrderInfoMapper;
import com.yuan.entity.OrderInfo;
import com.yuan.error.MyEmError;
import com.yuan.error.MyException;
import com.yuan.service.ItemService;
import com.yuan.service.OrderService;
import com.yuan.service.UserService;
import com.yuan.service.model.ItemModel;
import com.yuan.service.model.OrderModel;
import com.yuan.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws MyException {
        //1，校验下单状态：下单商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new MyException(MyEmError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null) {
            throw new MyException(MyEmError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }
        if (amount <= 0 || amount > 99) {
            throw new MyException(MyEmError.PARAMETER_VALIDATION_ERROR, "数量信息不正确");
        }
        //2，落单减库存
        boolean result = itemService.decrStock(itemId, amount);
        if (!result) {
            throw new MyException(MyEmError.STOCK_NOT_ENOUGH);
        }
        //3，订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));
        //生成id

        OrderInfo orderInfo = this.changeFromModelToOrderInfo(orderModel);
        orderInfoMapper.insertSelective(orderInfo);
        //4，返回前端
        return null;
    }

    private String createOrderNo() {
        //规则1：16位
        //规则2：前8位是时间信息，年月日
        //规则3：中间6位为自增序列
        //规则4：最后2位为分库分表位
        return null;
    }

    private OrderInfo changeFromModelToOrderInfo(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderModel, orderInfo);
        return orderInfo;
    }

}
