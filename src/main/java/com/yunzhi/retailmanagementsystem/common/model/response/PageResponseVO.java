package com.yunzhi.retailmanagementsystem.common.model.response;

import lombok.Data;

@Data
public class PageResponseVO<T> {
    private T data;
    private PaginationVO pagination;

    public PageResponseVO(T data, PaginationVO pagination) {
        this.data = data;
        this.pagination = pagination;
    }
}