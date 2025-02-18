package com.yunzhi.retailmanagementsystem.Mapper;

import com.yunzhi.retailmanagementsystem.model.domain.po.Reports;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Date;
import java.util.List;
/**
* @author Chloe
* @description 针对表【reports】的数据库操作Mapper
* @createDate 2025-01-12 17:32:16
* @Entity generator.domain.Reports
*/
public interface ReportsMapper extends BaseMapper<Reports> {
    // 根据用户ID查询报表列表
    List<Reports> selectByUserId(String userId);

    // 根据报表生成日期范围查询报表列表
    List<Reports> selectByGeneratedDateRange(Date startDate, Date endDate);

    // 根据报表ID查询报表
    Reports selectByReportId(String reportId);
}




