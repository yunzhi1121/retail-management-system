package com.yunzhi.retailmanagementsystem.business.report.controller;

import com.yunzhi.retailmanagementsystem.business.report.model.dto.CustomerReportRequestDTO;
import com.yunzhi.retailmanagementsystem.business.report.model.dto.InventoryReportRequestDTO;
import com.yunzhi.retailmanagementsystem.business.report.model.dto.ReportQueryRequest;
import com.yunzhi.retailmanagementsystem.business.report.model.dto.SalesReportRequestDTO;
import com.yunzhi.retailmanagementsystem.business.report.model.vo.CustomerReportVO;
import com.yunzhi.retailmanagementsystem.business.report.model.vo.InventoryReportVO;
import com.yunzhi.retailmanagementsystem.business.report.model.vo.ReportVO;
import com.yunzhi.retailmanagementsystem.business.report.model.vo.SalesReportVO;
import com.yunzhi.retailmanagementsystem.business.report.service.ReportsService;
import com.yunzhi.retailmanagementsystem.common.constant.security.Permission;
import com.yunzhi.retailmanagementsystem.common.model.response.BaseResponse;
import com.yunzhi.retailmanagementsystem.common.model.response.PageResponseVO;
import com.yunzhi.retailmanagementsystem.common.model.response.ResultUtils;
import com.yunzhi.retailmanagementsystem.common.security.authorization.RequiresPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportsService reportsService;

    @PostMapping("/sales")
    @RequiresPermission(Permission.REPORTS_SALES)
    public BaseResponse<List<SalesReportVO>> generateSalesReport(
            @RequestBody SalesReportRequestDTO request) {
        List<SalesReportVO> reports = reportsService.generateSalesReport(request);
        return ResultUtils.success(reports, "销售报表生成成功");
    }

    @PostMapping("/inventory")
    @RequiresPermission(Permission.REPORTS_INVENTORY)
    public BaseResponse<List<InventoryReportVO>> generateInventoryReport(
            @RequestBody InventoryReportRequestDTO request) {
        List<InventoryReportVO> reports = reportsService.generateInventoryReport(request);
        return ResultUtils.success(reports, "库存报表生成成功");
    }

    @PostMapping("/customer")
    @RequiresPermission(Permission.REPORTS_CUSTOMERS)
    public BaseResponse<List<CustomerReportVO>> generateCustomerReport(
            @RequestBody CustomerReportRequestDTO request) {
        List<CustomerReportVO> reports = reportsService.generateCustomerReport(request);
        return ResultUtils.success(reports, "客户报表生成成功");
    }

//    @PostMapping("/customer/export")
//    public void exportCustomerReport(@RequestBody CustomerReportRequestDTO request,
//                                     HttpServletResponse response) {
//        List<CustomerReportVO> data = reportsService.generateCustomerReport(request);
//        EasyExcel.write(response.getOutputStream(), CustomerReportVO.class)
//                .sheet("客户报表")
//                .doWrite(data);
//    }

    @GetMapping("/query")
    @RequiresPermission(Permission.REPORTS_READ)
    public BaseResponse<PageResponseVO<List<ReportVO>>> queryReports(
            @ModelAttribute ReportQueryRequest request) {
        PageResponseVO<List<ReportVO>> result = reportsService.queryReportsWithPagination(request);
        return ResultUtils.success(result, "分页查询成功");
    }
}