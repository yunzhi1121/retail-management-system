package com.yunzhi.retailmanagementsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.Mapper.GoodsMapper;
import com.yunzhi.retailmanagementsystem.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.model.domain.po.Goods;
import com.yunzhi.retailmanagementsystem.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * 货物管理服务实现类
 */
@Service
@Slf4j
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Override
    public Goods addGoods(String name, String description, Integer quantity, BigDecimal price) {
        // 参数校验
        validateGoodsParameters(name, quantity, price);

        // 检查是否已存在同名商品
        if (lambdaQuery().eq(Goods::getName, name).exists()) {
            throw new BusinessException(ErrorCode.GOODS_EXISTS, "商品名称已存在");
        }

        Goods goods = new Goods();
        goods.setGoodId(UUID.randomUUID().toString());
        goods.setName(name);
        goods.setDescription(description);
        goods.setQuantity(quantity);
        goods.setPrice(price);

        if (!save(goods)) {
            log.error("商品添加失败: {}", goods);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加商品失败");
        }

        return goods;
    }

    @Override
    public void updateGoods(String goodID, String name, String description, Integer quantity, BigDecimal price) {
        validateGoodsParameters(name, quantity, price);

        Goods goods = lambdaQuery().eq(Goods::getGoodId, goodID).oneOpt()
                .orElseThrow(() -> new BusinessException(ErrorCode.GOODS_NOT_FOUND, "商品ID不存在"));
        // 检查是否已存在同名商品
        if (lambdaQuery().eq(Goods::getName, name).exists()) {
            throw new BusinessException(ErrorCode.GOODS_EXISTS, "商品名称已存在");
        }
        boolean updated = lambdaUpdate()
                .eq(Goods::getGoodId, goodID)
                .set(name != null, Goods::getName, name)
                .set(description != null, Goods::getDescription, description)
                .set(quantity != null, Goods::getQuantity, quantity)
                .set(price != null, Goods::getPrice, price)
                .update();

        if (!updated) {
            throw new BusinessException(ErrorCode.UPDATE_FAILED, "商品更新失败");
        }
    }

    @Override
    public Goods getGoodsById(String goodID) {
        return lambdaQuery().eq(Goods::getGoodId, goodID).oneOpt()
                .orElseThrow(() -> new BusinessException(ErrorCode.GOODS_NOT_FOUND, "商品ID不存在"));
    }

    @Override
    public List<Goods> getGoodsByName(String name) {
        return lambdaQuery().like(Goods::getName, name).list();
    }

    private void validateGoodsParameters(String name, Integer quantity, BigDecimal price) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品名称不能为空");
        }
        if (quantity == null || quantity < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品数量不能小于 0");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品价格必须大于 0");
        }
    }
}
