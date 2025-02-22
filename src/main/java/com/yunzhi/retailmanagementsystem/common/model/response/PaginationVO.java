package com.yunzhi.retailmanagementsystem.common.model.response;

public record PaginationVO(
        int currentPage,
        int totalPages,
        int pageSize,
        int totalItems
) {}