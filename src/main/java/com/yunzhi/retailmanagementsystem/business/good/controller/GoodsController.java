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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "商品管理", description = "商品的增删改查操作")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Operation(summary = "添加商品", description = "根据传入的商品信息添加新商品")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "商品添加成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/add")
    @RequiresPermission(Permission.GOODS_CREATE)
    public BaseResponse<GoodsVO> addGoods(@RequestBody @Valid @Parameter(description = "商品创建信息", required = true) GoodsCreateDTO dto) {
        return ResultUtils.success(goodsService.addGoods(dto), "商品添加成功");
    }

    @Operation(summary = "更新商品信息", description = "根据商品 ID 和传入的更新信息更新商品信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "商品信息更新成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "商品 ID 不存在",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @PutMapping("/{goodID}")
    @RequiresPermission(Permission.GOODS_UPDATE)
    public BaseResponse<GoodsVO> updateGoods(@PathVariable @Parameter(description = "商品 ID", required = true) String goodID, @RequestBody @Valid @Parameter(description = "商品更新信息", required = true) GoodsUpdateDTO dto) {
        return ResultUtils.success(goodsService.updateGoods(goodID, dto), "商品信息更新成功");
    }

    @Operation(summary = "根据商品 ID 获取商品信息", description = "通过商品 ID 查询商品详细信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "商品查询成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "商品 ID 不存在",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/{goodID}")
    @RequiresPermission(Permission.GOODS_READ)
    public BaseResponse<GoodsVO> getGoodsById(@PathVariable @Parameter(description = "商品 ID", required = true) String goodID) {
        Goods goods = goodsService.getGoodsById(goodID);
        return ResultUtils.success(convertToVO(goods), "商品查询成功");
    }

    @Operation(summary = "根据条件查询商品列表", description = "根据传入的查询条件和分页信息查询商品列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "商品查询成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "服务器内部错误",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping
    @RequiresPermission(Permission.GOODS_READ)
    public BaseResponse<GoodsResponseVO> getGoods(@Parameter(description = "商品查询条件", required = false) GoodsQueryDTO goodsQueryDTO) {
        return ResultUtils.success(goodsService.getGoodsWithFilterAndPagination(goodsQueryDTO), "商品查询成功");
    }
}