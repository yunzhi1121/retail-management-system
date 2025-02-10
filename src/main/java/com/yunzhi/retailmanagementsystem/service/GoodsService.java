package com.yunzhi.retailmanagementsystem.service;

import com.yunzhi.retailmanagementsystem.model.domain.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Chloe
* @description 针对表【goods】的数据库操作Service
* @createDate 2025-01-12 17:32:29
*/
public interface GoodsService extends IService<Goods> {

    /**
     * 添加商品
     *
     * 此方法用于向系统中添加一个新的商品记录它需要商品的基本信息作为参数，
     * 包括名称、描述、数量和价格这些信息用于创建一个商品对象，并将其保存在系统中
     *
     * @param name 商品名称，一个字符串，表示商品的名称
     * @param description 商品描述，一个字符串，详细描述商品的特性或用途
     * @param quantity 商品数量，一个整数，表示库存的商品数量
     * @param price 商品价格，一个BigDecimal对象，表示商品的单价
     * @return 返回表示新添加的商品对象
     */
    Goods addGoods(String name, String description, Integer quantity, BigDecimal price);

    /**
     * 更新商品信息
     *
     * @param goodID 商品ID，用于唯一标识商品
     * @param name 商品名称
     * @param description 商品描述
     * @param quantity 商品数量
     * @param price 商品价格
     * @return 如果商品信息成功更新，返回true；否则返回false
     */
    boolean updateGoods(String goodID, String name, String description, Integer quantity, BigDecimal price);

    /**
     * 根据商品ID获取商品信息
     *
     * @param goodID 商品ID，用于唯一标识商品
     * @return 返回商品对象，如果找不到则返回null
     */
    Goods getGoodsById(String goodID);

    /**
     * 根据商品名称获取商品列表
     *
     * @param name 商品名称
     * @return 返回商品列表，如果找不到则返回空列表
     */
    List<Goods> getGoodsByName(String name);

}
