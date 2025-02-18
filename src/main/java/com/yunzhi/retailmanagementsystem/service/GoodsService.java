package com.yunzhi.retailmanagementsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunzhi.retailmanagementsystem.model.domain.po.Goods;

import java.math.BigDecimal;
import java.util.List;

/**
 * 货物管理服务接口
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 添加新商品
     *
     * @param name        商品名称
     * @param description 商品描述
     * @param quantity    商品数量
     * @param price       商品价格
     * @return 新增的商品对象
     */
    Goods addGoods(String name, String description, Integer quantity, BigDecimal price);

    /**
     * 更新商品信息
     *
     * @param goodID      商品ID
     * @param name        商品名称
     * @param description 商品描述
     * @param quantity    商品数量
     * @param price       商品价格
     */
    void updateGoods(String goodID, String name, String description, Integer quantity, BigDecimal price);

    /**
     * 根据商品ID获取商品信息
     *
     * @param goodID 商品ID
     * @return 商品对象
     */
    Goods getGoodsById(String goodID);

    /**
     * 根据商品名称获取商品列表，支持模糊查询
     *
     * @param name 商品名称
     * @return 匹配的商品列表
     */
    List<Goods> getGoodsByName(String name);
}
