package com.yunzhi.retailmanagementsystem.business.good.model.vo;

import com.yunzhi.retailmanagementsystem.business.good.model.po.Goods;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class GoodsVO {

    private String goodId;

    private String name;

    private String description;

    private Integer quantity;

    private BigDecimal price;
    public static GoodsVO fromGoods(Goods goods) {
        return new GoodsVO()
                .setGoodId(goods.getGoodId())
                .setName(goods.getName())
                .setDescription(goods.getDescription())
                .setQuantity(goods.getQuantity())
                .setPrice(goods.getPrice());
    }

}