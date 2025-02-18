package com.yunzhi.retailmanagementsystem.controller;

import com.yunzhi.retailmanagementsystem.model.domain.dto.GoodsCreateDTO;
import com.yunzhi.retailmanagementsystem.model.domain.dto.GoodsUpdateDTO;
import com.yunzhi.retailmanagementsystem.model.domain.po.Goods;
import com.yunzhi.retailmanagementsystem.model.domain.vo.GoodsResponseVO;
import com.yunzhi.retailmanagementsystem.model.domain.vo.GoodsVO;
import com.yunzhi.retailmanagementsystem.model.domain.vo.PaginationVO;
import com.yunzhi.retailmanagementsystem.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    //━━━━━━━━━━━━━━ 端点实现 ━━━━━━━━━━━━━━
    @PostMapping
    public BaseResponse<String> addGoods(@RequestBody @Valid GoodsCreateDTO dto) {
        Goods goods = goodsService.addGoods(dto.name(), dto.description(), dto.quantity(), dto.price());
        return ResultUtils.success(goods.getGoodId(), "商品添加成功");
    }

    @PutMapping("/{goodID}")
    public BaseResponse<Void> updateGoods(@PathVariable String goodID, @RequestBody @Valid GoodsUpdateDTO dto) {
        goodsService.updateGoods(goodID, dto.name(), dto.description(), dto.quantity(), dto.price());
        return ResultUtils.success("商品信息更新成功");
    }

    @GetMapping("/{goodID}")
    public BaseResponse<GoodsVO> getGoodsById(@PathVariable String goodID) {
        Goods goods = goodsService.getGoodsById(goodID);
        return ResultUtils.success(GoodsVO.fromGoods(goods), null);
    }

    @GetMapping
    public BaseResponse<GoodsResponseVO> getGoods(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        if (!isValidQueryParameters(minPrice, maxPrice)) {
            return ResultUtils.error(HttpStatus.BAD_REQUEST.value(), "查询参数无效", "最低价格不能大于最高价格");
        }

        List<Goods> goodsList = goodsService.getGoodsByName(name);

        if (minPrice != null || maxPrice != null) {
            goodsList = filterGoodsByPrice(goodsList, minPrice, maxPrice);
        }

        List<Goods> paginatedGoodsList = paginateGoodsList(goodsList, page, pageSize);

        List<GoodsVO> goodsVOs = paginatedGoodsList.stream()
                .map(GoodsVO::fromGoods)
                .collect(Collectors.toList());

        PaginationVO pagination = new PaginationVO(
                page,
                (int) Math.ceil((double) goodsList.size() / pageSize),
                pageSize,
                goodsList.size()
        );

        GoodsResponseVO responseVO = new GoodsResponseVO(goodsVOs, pagination);
        return ResultUtils.success(responseVO, null);
    }

    //━━━━━━━━━━━━━━ 工具方法 ━━━━━━━━━━━━━━
    private boolean isValidQueryParameters(BigDecimal minPrice, BigDecimal maxPrice) {
        return minPrice == null || maxPrice == null || minPrice.compareTo(maxPrice) <= 0;
    }

    private List<Goods> filterGoodsByPrice(List<Goods> goodsList, BigDecimal minPrice, BigDecimal maxPrice) {
        return goodsList.stream()
                .filter(goods -> {
                    boolean meetsMinPrice = minPrice == null || goods.getPrice().compareTo(minPrice) >= 0;
                    boolean meetsMaxPrice = maxPrice == null || goods.getPrice().compareTo(maxPrice) <= 0;
                    return meetsMinPrice && meetsMaxPrice;
                })
                .collect(Collectors.toList());
    }

    private List<Goods> paginateGoodsList(List<Goods> goodsList, int page, int pageSize) {
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, goodsList.size());
        return goodsList.subList(startIndex, endIndex);
    }
}