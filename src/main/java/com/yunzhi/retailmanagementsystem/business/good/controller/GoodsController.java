package com.yunzhi.retailmanagementsystem.business.good.controller;

import com.yunzhi.retailmanagementsystem.business.good.model.dto.GoodsCreateDTO;
import com.yunzhi.retailmanagementsystem.business.good.model.dto.GoodsQueryDTO;
import com.yunzhi.retailmanagementsystem.business.good.model.dto.GoodsUpdateDTO;
import com.yunzhi.retailmanagementsystem.business.good.model.po.Goods;
import com.yunzhi.retailmanagementsystem.business.good.model.vo.GoodsResponseVO;
import com.yunzhi.retailmanagementsystem.business.good.model.vo.GoodsVO;
import com.yunzhi.retailmanagementsystem.business.good.service.GoodsService;
import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;
import com.yunzhi.retailmanagementsystem.common.model.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.common.model.response.PaginationVO;
import com.yunzhi.retailmanagementsystem.common.model.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.common.security.authorization.RequiresPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.yunzhi.retailmanagementsystem.common.utils.coverter.VOConverter.convertToVO;

@Validated
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @PostMapping("/add")
    @RequiresPermission(Permission.GOODS_CREATE)
    public BaseResponse<GoodsVO> addGoods(@RequestBody @Valid GoodsCreateDTO dto) {
        return ResultUtils.success(goodsService.addGoods(dto), "商品添加成功");
    }

    @PutMapping("/{goodID}")
    @RequiresPermission(Permission.GOODS_UPDATE)
    public BaseResponse<GoodsVO> updateGoods(@PathVariable String goodID, @RequestBody @Valid GoodsUpdateDTO dto) {
        return ResultUtils.success(goodsService.updateGoods(goodID, dto),"商品信息更新成功");
    }

    @GetMapping("/{goodID}")
    @RequiresPermission(Permission.GOODS_READ)
    public BaseResponse<GoodsVO> getGoodsById(@PathVariable String goodID) {
        Goods goods = goodsService.getGoodsById(goodID);
        return ResultUtils.success(convertToVO(goods), "商品查询成功");
    }

    @GetMapping
    @RequiresPermission(Permission.GOODS_READ)
    public BaseResponse<GoodsResponseVO> getGoods(GoodsQueryDTO goodsQueryDTO) {
        return ResultUtils.success(goodsService.getGoodsWithFilterAndPagination(goodsQueryDTO), "商品查询成功");
    }
}