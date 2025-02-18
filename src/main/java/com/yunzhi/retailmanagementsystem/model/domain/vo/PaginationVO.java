package com.yunzhi.retailmanagementsystem.model.domain.vo;

public record PaginationVO(
        int currentPage,
        int totalPages,
        int pageSize,
        int totalItems
) {}