package com.yunzhi.retailmanagementsystem.business.report.service;

import com.yunzhi.retailmanagementsystem.business.report.model.dto.*;
import com.yunzhi.retailmanagementsystem.business.report.model.po.Reports;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunzhi.retailmanagementsystem.business.report.model.vo.CustomerReportVO;
import com.yunzhi.retailmanagementsystem.business.report.model.vo.InventoryReportVO;
import com.yunzhi.retailmanagementsystem.business.report.model.vo.ReportVO;
import com.yunzhi.retailmanagementsystem.business.report.model.vo.SalesReportVO;
import com.yunzhi.retailmanagementsystem.common.model.response.PageResponseVO;

import java.util.List;

/**
* @author Chloe
* @description 针对表【reports】的数据库操作Service
* @createDate 2025-01-12 17:32:17
*/
public interface ReportsService extends IService<Reports> {
    // 生成销售报表
    public List<SalesReportVO> generateSalesReport(SalesReportRequestDTO request);

    // 生成库存报表
    public List<InventoryReportVO> generateInventoryReport(InventoryReportRequestDTO request);

    // 生成客户行为报表
    public List<CustomerReportVO> generateCustomerReport(CustomerReportRequestDTO request);

    // 查询报表
    public PageResponseVO<List<ReportVO>> queryReportsWithPagination(ReportQueryRequest request);
}
