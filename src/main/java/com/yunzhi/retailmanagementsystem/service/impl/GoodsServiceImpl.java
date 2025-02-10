package com.yunzhi.retailmanagementsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.model.domain.Goods;
import com.yunzhi.retailmanagementsystem.service.GoodsService;
import com.yunzhi.retailmanagementsystem.Mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
* @author Chloe
* @description 针对表【goods】的数据库操作Service实现
* @createDate 2025-01-12 17:32:29
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService{

    @Autowired
    private GoodsMapper goodsMapper;

    public Goods addGoods(String name, String description, Integer quantity, BigDecimal price) {
        // 先查询数据库中是否存在同名商品
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goods::getName, name);
        List<Goods> existingGoods = this.list(queryWrapper);

        if (!existingGoods.isEmpty()) {
            // 如果存在同名商品，返回 null 表示添加失败
            return null;
        }

        String goodID = UUID.randomUUID().toString();
        Goods goods = new Goods();
        goods.setGoodID(goodID);

        goods.setName(name);
        goods.setDescription(description);
        goods.setQuantity(quantity);
        goods.setPrice(price);

        // 尝试保存商品信息
        if (this.save(goods)) {
            return goods; // 返回保存成功的商品对象，包含 goodID
        } else {
            return null; // 如果保存失败，返回 null
        }
    }

    public boolean updateGoods(String goodID, String name, String description, Integer quantity, BigDecimal price) {
        Goods goods = new Goods();
        goods.setGoodID(goodID);
        goods.setName(name);
        goods.setDescription(description);
        goods.setQuantity(quantity);
        goods.setPrice(price);
        return this.updateById(goods);
    }

    public Goods getGoodsById(String goodID) {
        return this.getById(goodID);
    }

    public List<Goods> getGoodsByName(String name) {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        // 使用 like 方法构建模糊查询条件
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Goods::getName, name);
        }
        return this.list(queryWrapper);
    }
}




