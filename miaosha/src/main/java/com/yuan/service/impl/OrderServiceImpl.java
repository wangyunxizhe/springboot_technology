package com.yuan.service.impl;

import com.yuan.dao.OrderInfoMapper;
import com.yuan.dao.SequInfoMapper;
import com.yuan.entity.OrderInfo;
import com.yuan.entity.SequInfo;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private SequInfoMapper sequInfoMapper;

    //创建订单
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
        orderModel.setId(this.createOrderNo());
        OrderInfo orderInfo = this.changeFromModelToOrderInfo(orderModel);
        orderInfoMapper.insertSelective(orderInfo);
        //4，增加商品销量
        itemService.addSales(itemId, amount);
        //5，返回
        return orderModel;
    }

    /**
     * 该注解的意义：因为createOrderNo()方法是被createOrder()方法调用，createOrder()该方法是个Transactional方法，
     * 所以在发生错误时，所有该方法内的数据库操作都会被回滚，包括这里的自定义自增序列方法--createOrderNo()，
     * 但是序列操作原则上是无论成功与否，该自增值都只会出现一次，所以这里无论方法是否成功，
     * createOrderNo()中的数据库操作都必须提交，所以在createOrderNo()方法上开启一个新的事务，
     * 跟createOrder()方法中的事务区别开，就算createOrder()执行失败，
     * createOrderNo()也一样会进行数据库的提交操作。
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String createOrderNo() {
        //规则1：16位
        StringBuilder sb = new StringBuilder();
        //规则2：前8位是时间信息，年月日
        LocalDateTime now = LocalDateTime.now();//java8中的新类
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        sb.append(nowDate);
        //规则3：中间6位为自定义自增序列
        int sequence = 0;
        SequInfo sequInfo = sequInfoMapper.getSequenceByName("t_miaosha_order_info");
        sequence = sequInfo.getCurrentValue();//获取当前自定义序列的值
        //获取后一定要将现在的序列值+表中定义的步长，以便下次获取时实现了自增的效果
        sequInfo.setCurrentValue(sequInfo.getCurrentValue() + sequInfo.getStep());
        sequInfoMapper.updateByPrimaryKeySelective(sequInfo);//同步到数据库
        String sequenceStr = String.valueOf(sequence);
        //因为需要6位，所以不够需要补足，这里用0补足
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            sb.append(0);
        }
        sb.append(sequenceStr);
        //规则4：最后2位为分库分表位，暂时写死
        sb.append("00");
        return sb.toString();
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
