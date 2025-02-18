package com.yunzhi.retailmanagementsystem.model.domain.vo;

import java.util.List;

public record GoodsResponseVO(
        List<GoodsVO> goods,
        PaginationVO pagination
) {}