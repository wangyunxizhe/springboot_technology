package com.yuan.controller;

import com.yuan.controller.viewObj.ItemVO;
import com.yuan.error.MyException;
import com.yuan.response.CommonReturnType;
import com.yuan.service.ItemService;
import com.yuan.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller("item")
@RequestMapping("/item")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("toCreate")
    public String toCreate() {
        return "createItem";
    }

    //创建商品
    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "imgUrl") String imgUrl) throws MyException {
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);
        ItemModel itemModel1RS = itemService.createItem(itemModel);
        ItemVO itemVO = changeFromItemModel(itemModel1RS);
        return CommonReturnType.create(itemVO);
    }

    private ItemVO changeFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);//将itemModel中的属性值，copy到itemVO的相同属性里
        return itemVO;
    }

    @RequestMapping("toGetItem")
    public String toGetItem(@RequestParam(name = "id") Integer id) {
        return "getItem";
    }

    //商品详情页浏览
    @RequestMapping(value = "/getItem", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id) {
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = changeFromItemModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    @RequestMapping("toListItem")
    public String toListItem() {
        return "listItem";
    }

    //商品列表页浏览
    @RequestMapping(value = "/listItem", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem() {
        List<ItemModel> itemModels = itemService.listItem();
        List<ItemVO> itemVOS = itemModels.stream().map(itemModel -> {
            ItemVO itemVO = this.changeFromItemModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOS);
    }

}
