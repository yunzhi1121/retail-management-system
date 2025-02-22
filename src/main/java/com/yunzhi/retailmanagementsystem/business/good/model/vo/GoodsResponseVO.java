package com.yunzhi.retailmanagementsystem.business.good.model.vo;

import com.yunzhi.retailmanagementsystem.common.model.response.PaginationVO;
import lombok.Data;

import java.util.List;

@Data
public class GoodsResponseVO {

    private List<GoodsVO> goodsVO;

    private PaginationVO pagination;

    public GoodsResponseVO(List<GoodsVO> goodsVOs, PaginationVO pagination) {
        this.goodsVO = goodsVOs;
        this.pagination = pagination;
    }
}