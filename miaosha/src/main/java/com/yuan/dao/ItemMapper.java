package com.yuan.dao;

import com.yuan.entity.Item;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Item record);

    int insertSelective(Item record);

    Item selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);

    //获取商品列表
    List<Item> listItem();

    //增加商品销量
    int addSales(@Param("id") Integer id, @Param("amount") Integer amount);
}