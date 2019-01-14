package com.yuan.dao;

import com.yuan.entity.ItemStock;
import org.apache.ibatis.annotations.Param;

public interface ItemStockMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemStock record);

    int insertSelective(ItemStock record);

    ItemStock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemStock record);

    int updateByPrimaryKey(ItemStock record);

    //根据item_id获取表数据
    ItemStock selectByItemId(Integer id);

    //库存扣减
    int decrStock(@Param("itemId") Integer itemId, @Param("amount") Integer amount);
}