package com.yunzhi.retailmanagementsystem.service;

import com.yunzhi.retailmanagementsystem.exception.BusinessException;

public interface InventoryService {
    /**
     * 商品入库
     */
    void addInventory(String goodID, int quantity) throws BusinessException;

    /**
     * 商品出库
     */
    void reduceInventory(String goodID, int quantity) throws BusinessException;

    /**
     * 查询库存
     */
    int getInventoryStatus(String goodID) throws BusinessException;
}