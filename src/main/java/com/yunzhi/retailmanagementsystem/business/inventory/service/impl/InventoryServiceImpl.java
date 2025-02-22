package com.yunzhi.retailmanagementsystem.business.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.business.good.mapper.GoodsMapper;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.business.good.model.po.Goods;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.business.inventory.service.InventoryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements InventoryService {

    @Override
    public void addInventory(String goodID, int quantity) {
        if(quantity <= 0){
            throw new BusinessException(ErrorCode.PARAM_ERROR, "库存数量必须大于0");
        }
        Goods goods = getGoodsById(goodID);
        goods.setQuantity(goods.getQuantity() + quantity);
        try {
            updateById(goods);
        } catch (Exception e) {
            log.error("Error : ", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "库存更新失败");
        }
    }

    @Override
    public void reduceInventory(String goodID, int quantity) {
        Goods goods = getGoodsById(goodID);
        if (goods.getQuantity() < quantity) {
            throw new BusinessException(ErrorCode.STOCK_INSUFFICIENT, "库存更新失败");
        }
        goods.setQuantity(goods.getQuantity() - quantity);
        try {
            updateById(goods);
        } catch (Exception e) {
            log.error("Error : ", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "库存更新失败");

        }
    }

    @Override
    public int getInventoryStatus(String goodID) {
        return getGoodsById(goodID).getQuantity();
    }

    private Goods getGoodsById(String goodID) {
        Goods goods = lambdaQuery().eq(Goods::getGoodId, goodID).one();
        if (goods == null) {
            throw new BusinessException(ErrorCode.GOODS_NOT_FOUND, "请检查商品ID是否正确");
        }
        return goods;
    }
}