package com.yuan.service;

import com.yuan.service.model.PromoModel;

public interface PromoService {

    //获取商品活动信息
    PromoModel getPromoByItemId(Integer itemId);

}
