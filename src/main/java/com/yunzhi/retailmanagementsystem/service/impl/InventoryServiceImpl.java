package com.yunzhi.retailmanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.Mapper.GoodsMapper;
import com.yunzhi.retailmanagementsystem.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.model.domain.po.Goods;
import com.yunzhi.retailmanagementsystem.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.service.InventoryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements InventoryService {

    @Override
    public void addInventory(String goodID, int quantity) {
        Goods goods = getGoodsById(goodID);
        goods.setQuantity(goods.getQuantity() + quantity);
        if (!updateById(goods)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "库存更新失败");
        }
    }

    @Override
    public void reduceInventory(String goodID, int quantity) {
        Goods goods = getGoodsById(goodID);
        if (goods.getQuantity() < quantity) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }
        goods.setQuantity(goods.getQuantity() - quantity);
        if (!updateById(goods)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "库存更新失败");
        }
    }

    @Override
    public int getInventoryStatus(String goodID) {
        return getGoodsById(goodID).getQuantity();
    }

    private Goods getGoodsById(String goodID) {
        Goods goods = lambdaQuery().eq(Goods::getGoodId, goodID).one();
        if (goods == null) {
            throw new BusinessException(ErrorCode.GOODS_NOT_FOUND);
        }
        return goods;
    }
}