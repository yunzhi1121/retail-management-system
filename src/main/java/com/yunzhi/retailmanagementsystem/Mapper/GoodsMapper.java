package com.yunzhi.retailmanagementsystem.Mapper;

import com.yunzhi.retailmanagementsystem.model.domain.po.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
/**
* @author Chloe
* @description 针对表【goods】的数据库操作Mapper
* @createDate 2025-01-12 17:32:29
* @Entity generator.domain.Goods
*/
public interface GoodsMapper extends BaseMapper<Goods> {
    // 根据商品名称模糊查询商品列表
    List<Goods> selectByNameLike(String name);

    // 根据商品ID查询商品
    Goods selectByGoodId(String goodId);

    // 根据商品库存数量范围查询商品列表
    List<Goods> selectByQuantityRange(int minQuantity, int maxQuantity);
}




