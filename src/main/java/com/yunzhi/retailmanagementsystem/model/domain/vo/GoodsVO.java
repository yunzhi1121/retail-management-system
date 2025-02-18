package com.yunzhi.retailmanagementsystem.model.domain.vo;

import com.yunzhi.retailmanagementsystem.model.domain.po.Goods;

import java.math.BigDecimal;

public record GoodsVO(
        String goodId,
        String name,
        String description,
        Integer quantity,
        BigDecimal price
) {
    public static GoodsVO fromGoods(Goods goods) {
        return new GoodsVO(
                goods.getGoodId(),
                goods.getName(),
                goods.getDescription(),
                goods.getQuantity(),
                goods.getPrice()
        );
    }
}