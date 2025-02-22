package com.yunzhi.retailmanagementsystem.business.good.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunzhi.retailmanagementsystem.business.good.model.dto.GoodsCreateDTO;
import com.yunzhi.retailmanagementsystem.business.good.model.dto.GoodsQueryDTO;
import com.yunzhi.retailmanagementsystem.business.good.model.dto.GoodsUpdateDTO;
import com.yunzhi.retailmanagementsystem.business.good.model.po.Goods;
import com.yunzhi.retailmanagementsystem.business.good.model.vo.GoodsResponseVO;
import com.yunzhi.retailmanagementsystem.business.good.model.vo.GoodsVO;

import java.util.List;

/**
 * 货物管理服务接口
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 添加新商品
     *
     * @param goodsCreateDTO@return 新增的商品对象
     */
    GoodsVO addGoods(GoodsCreateDTO goodsCreateDTO);

    /**
     * 更新商品信息
     *
     * @param goodId
     * @param goodsUpdateDTO
     */
    GoodsVO updateGoods(String goodId, GoodsUpdateDTO goodsUpdateDTO);

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
    GoodsResponseVO getGoodsWithFilterAndPagination(GoodsQueryDTO goodsQueryDTO);
}
