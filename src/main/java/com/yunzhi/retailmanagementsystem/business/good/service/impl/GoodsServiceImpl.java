package com.yunzhi.retailmanagementsystem.business.good.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunzhi.retailmanagementsystem.business.good.mapper.GoodsMapper;
import com.yunzhi.retailmanagementsystem.business.good.model.dto.GoodsCreateDTO;
import com.yunzhi.retailmanagementsystem.business.good.model.dto.GoodsQueryDTO;
import com.yunzhi.retailmanagementsystem.business.good.model.dto.GoodsUpdateDTO;
import com.yunzhi.retailmanagementsystem.business.good.model.vo.GoodsResponseVO;
import com.yunzhi.retailmanagementsystem.business.good.model.vo.GoodsVO;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.business.good.model.po.Goods;
import com.yunzhi.retailmanagementsystem.business.good.service.GoodsService;
import com.yunzhi.retailmanagementsystem.common.model.response.PaginationVO;
import com.yunzhi.retailmanagementsystem.common.utils.coverter.VOConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 货物管理服务实现类
 */
@Service
@Slf4j
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Override
    public GoodsVO addGoods(GoodsCreateDTO goodsCreateDTO) {
        // 参数校验
        validateGoodsParameters(goodsCreateDTO.getName(), goodsCreateDTO.getQuantity(), goodsCreateDTO.getPrice());

        // 检查是否已存在同名商品
        if (lambdaQuery().eq(Goods::getName, goodsCreateDTO.getName()).exists()) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "商品名称已存在");
        }

        Goods goods = new Goods();
        goods.setGoodId(UUID.randomUUID().toString());
        goods.setName(goodsCreateDTO.getName());
        goods.setDescription(goodsCreateDTO.getDescription());
        goods.setQuantity(goodsCreateDTO.getQuantity());
        goods.setPrice(goodsCreateDTO.getPrice());

        try {
            save(goods);
        } catch (Exception e) {
            log.error("Error : ", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "添加商品失败");
        }



        return VOConverter.convertToVO(goods);
    }

    @Override
    public GoodsVO updateGoods(String goodId, GoodsUpdateDTO goodsUpdateDTO) {
        validateGoodsParameters(goodsUpdateDTO.getName(), goodsUpdateDTO.getQuantity(), goodsUpdateDTO.getPrice());

        lambdaQuery().eq(Goods::getGoodId, goodId).oneOpt()
                .orElseThrow(() -> new BusinessException(ErrorCode.GOODS_NOT_FOUND, "商品ID不存在"));
        // 检查是否已存在同名商品
        if (lambdaQuery().eq(Goods::getName, goodsUpdateDTO.getName()).exists()) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "商品名称已存在");
        }
        try {
            lambdaUpdate()
                    .eq(Goods::getGoodId, goodId)
                    .set(goodsUpdateDTO.getName() != null, Goods::getName, goodsUpdateDTO.getName())
                    .set(goodsUpdateDTO.getDescription() != null, Goods::getDescription, goodsUpdateDTO.getDescription())
                    .set(goodsUpdateDTO.getQuantity() != null, Goods::getQuantity, goodsUpdateDTO.getQuantity())
                    .set(goodsUpdateDTO.getPrice() != null, Goods::getPrice, goodsUpdateDTO.getPrice())
                    .update();
        } catch (Exception e) {
            log.error("Error : ", e);
            throw new BusinessException(ErrorCode.DB_OPERATION_FAILED, "商品更新失败");
        }
        // 重新从数据库中获取最新的商品信息
        Goods good = lambdaQuery().eq(Goods::getGoodId, goodId).one();
        return VOConverter.convertToVO(good);
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

    @Override
    public GoodsResponseVO getGoodsWithFilterAndPagination(GoodsQueryDTO goodsQueryDTO) {
        if (!isValidQueryParameters(goodsQueryDTO.getMinPrice(), goodsQueryDTO.getMaxPrice())) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "最低价格不能大于最高价格");
        }
        List<Goods> goodsList;
        if (goodsQueryDTO.getName() != null) {
            goodsList = getGoodsByName(goodsQueryDTO.getName());
        } else {
            goodsList = list();
        }

        if (goodsQueryDTO.getMinPrice() != null || goodsQueryDTO.getMaxPrice() != null) {
            goodsList = filterGoodsByPrice(goodsList, goodsQueryDTO.getMinPrice(), goodsQueryDTO.getMaxPrice());
        }

        List<Goods> paginatedGoodsList = paginateGoodsList(goodsList, goodsQueryDTO.getPage(), goodsQueryDTO.getPageSize());

        List<GoodsVO> goodsVOs = paginatedGoodsList.stream()
                .map(GoodsVO::fromGoods)
                .collect(Collectors.toList());

        PaginationVO pagination = new PaginationVO(
                goodsQueryDTO.getPage(),
                (int) Math.ceil((double) goodsList.size() / goodsQueryDTO.getPageSize()),
                goodsQueryDTO.getPageSize(),
                goodsList.size()
        );
        return new GoodsResponseVO(goodsVOs, pagination);
    }

    //━━━━━━━━━━━━━━ 工具方法 ━━━━━━━━━━━━━━
    private void validateGoodsParameters(String name, Integer quantity, BigDecimal price) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "商品名称不能为空");
        }
        if (quantity == null || quantity < 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "商品数量不能小于 0");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "商品价格必须大于 0");
        }
    }
    private boolean isValidQueryParameters(BigDecimal minPrice, BigDecimal maxPrice) {
        return minPrice == null || maxPrice == null || minPrice.compareTo(maxPrice) <= 0;
    }

    private List<Goods> filterGoodsByPrice(List<Goods> goodsList, BigDecimal minPrice, BigDecimal maxPrice) {
        LambdaQueryWrapper<Goods> queryWrapper = Wrappers.lambdaQuery();
        if (minPrice != null) {
            queryWrapper.ge(Goods::getPrice, minPrice);
        }
        if (maxPrice != null) {
            queryWrapper.le(Goods::getPrice, maxPrice);
        }
        return goodsList.stream().filter(good -> {
            boolean meetsMin = minPrice == null || good.getPrice().compareTo(minPrice) >= 0;
            boolean meetsMax = maxPrice == null || good.getPrice().compareTo(maxPrice) <= 0;
            return meetsMin && meetsMax;
        }).collect(Collectors.toList());
    }

    private List<Goods> paginateGoodsList(List<Goods> goodsList, int page, int pageSize) {
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, goodsList.size());
        return goodsList.subList(fromIndex, toIndex);
    }


}
