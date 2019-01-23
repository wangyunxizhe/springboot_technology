package com.yuan.service.impl;

import com.yuan.dao.PromoInfoMapper;
import com.yuan.entity.PromoInfo;
import com.yuan.service.PromoService;
import com.yuan.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoInfoMapper promoDao;

    //获取商品活动信息
    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        //获取对应商品的秒杀活动信息
        PromoInfo promoInfo = promoDao.selectByItemId(itemId);
        PromoModel promoModel = changeFromPromoEntiy(promoInfo);
        if (promoModel == null) {
            return null;
        }
        //判断当前时间秒杀活动是否即将开始，或者正在进行
        if (promoModel.getStartDate().isAfterNow()) {//开始时间晚于现在
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){//结束时间早于现在时间
            promoModel.setStatus(3);
        }else {
            promoModel.setStatus(2);
        }
        return promoModel;
    }

    //将PromoInfo中的属性值，封装到PromoModel中
    private PromoModel changeFromPromoEntiy(PromoInfo promoInfo) {
        if (promoInfo == null) {
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoInfo, promoModel);//将PromoInfo中的属性值，copy到PromoModel的相同属性里
        //因为2个类中变量类型不同，无法copy，这里手动set
        promoModel.setStartDate(new DateTime(promoInfo.getStartDate()));
        promoModel.setEndDate(new DateTime(promoInfo.getEndDate()));
        return promoModel;
    }

}
