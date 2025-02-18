package com.yunzhi.retailmanagementsystem.controller;

import com.yunzhi.retailmanagementsystem.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public BaseResponse<?> addInventory(@RequestParam String goodID, @RequestParam @Min(1) int quantity) {
        inventoryService.addInventory(goodID, quantity);
        return ResultUtils.success("货物入库成功");
    }

    @PostMapping("/reduce")
    public BaseResponse<?> reduceInventory(@RequestParam String goodID, @RequestParam @Min(1) int quantity) {
        inventoryService.reduceInventory(goodID, quantity);
        return ResultUtils.success("货物出库成功");
    }

    @GetMapping("/status")
    public BaseResponse<Integer> getInventoryStatus(@RequestParam String goodID) {
        int quantity = inventoryService.getInventoryStatus(goodID);
        return ResultUtils.success(quantity, "库存状态获取成功");
    }
}