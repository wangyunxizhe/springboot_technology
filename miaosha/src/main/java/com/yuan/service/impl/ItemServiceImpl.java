package com.yuan.service.impl;

import com.yuan.dao.ItemMapper;
import com.yuan.dao.ItemStockMapper;
import com.yuan.entity.Item;
import com.yuan.entity.ItemStock;
import com.yuan.error.MyEmError;
import com.yuan.error.MyException;
import com.yuan.service.ItemService;
import com.yuan.service.model.ItemModel;
import com.yuan.validator.ValidationResult;
import com.yuan.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemStockMapper itemStockMapper;

    //创建商品
    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws MyException {
        ValidationResult result = validator.validate(itemModel);//参数校验
        if (result.isHasErrors()) {//参数有问题，将详细问题抛出
            throw new MyException(MyEmError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }
        Item item = this.changeFromModel(itemModel);
        itemMapper.insertSelective(item);
        itemModel.setId(item.getId());
        ItemStock itemStock = this.changeFromModelToStock(itemModel);
        itemStockMapper.insertSelective(itemStock);
        return this.getItemById(itemModel.getId());//返回创建完成的对象
    }

    //将ItemModel中的部分属性值，封装到Item中
    public Item changeFromModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemModel, item);//将itemModel中的属性值，copy到itme的相同属性里
        return item;
    }

    //将ItemModel中的部分属性值，封装到ItemStock中
    public ItemStock changeFromModelToStock(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStock itemStock = new ItemStock();
        itemStock.setItemId(itemModel.getId());
        itemStock.setStock(itemModel.getStock());
        return itemStock;
    }

    //商品列表浏览
    @Override
    public List<ItemModel> listItem() {
        List<Item> itemList = itemMapper.listItem();
        //将List<Item>转为List<ItemModel>，运用Java8中的新写法
        List<ItemModel> itemModels = itemList.stream().map(item -> {
            ItemStock itemStock = itemStockMapper.selectByItemId(item.getId());
            ItemModel itemModel = this.changeFromItemEntity(item, itemStock);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModels;
    }

    //商品详情浏览
    @Override
    public ItemModel getItemById(Integer id) {
        Item item = itemMapper.selectByPrimaryKey(id);
        if (item == null) {
            return null;
        }
        //获取该商品的库存数量
        ItemStock itemStock = itemStockMapper.selectByItemId(id);
        ItemModel itemModel = this.changeFromItemEntity(item, itemStock);
        return itemModel;
    }

    //将UserInfo，UserPwd中的属性值，封装到UserModel中
    private ItemModel changeFromItemEntity(Item item, ItemStock itemStock) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(item, itemModel);//将item中的属性值，copy到itemModel的相同属性里
        itemModel.setStock(itemStock.getStock());
        return itemModel;
    }

    //库存扣减
    @Override
    @Transactional
    public boolean decrStock(Integer itemId, Integer amount) throws MyException {
        int affectedRow = itemStockMapper.decrStock(itemId, amount);//受影响行数
        if (affectedRow > 0) {//受影响行数大于0，说明更新成功
            return true;
        }
        return false;
    }

}
