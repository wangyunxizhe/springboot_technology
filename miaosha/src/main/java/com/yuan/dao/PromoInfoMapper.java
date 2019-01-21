package com.yuan.dao;

import com.yuan.entity.PromoInfo;

public interface PromoInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PromoInfo record);

    int insertSelective(PromoInfo record);

    PromoInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PromoInfo record);

    int updateByPrimaryKey(PromoInfo record);

    PromoInfo selectByItemId(Integer itemId);
}