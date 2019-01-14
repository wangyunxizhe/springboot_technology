package com.yuan.service;

import com.yuan.error.MyException;
import com.yuan.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    //创建商品
    ItemModel createItem(ItemModel itemModel) throws MyException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);

    //库存扣减
    boolean decrStock(Integer itemId, Integer amount) throws MyException;

}
