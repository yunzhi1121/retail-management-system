package com.yunzhi.retailmanagementsystem.business.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunzhi.retailmanagementsystem.business.customer.mapper.CustomersMapper;
import com.yunzhi.retailmanagementsystem.business.customer.model.po.Customers;
import com.yunzhi.retailmanagementsystem.business.good.mapper.GoodsMapper;
import com.yunzhi.retailmanagementsystem.business.good.model.po.Goods;
import com.yunzhi.retailmanagementsystem.business.order.mapper.OrderGoodMapper;
import com.yunzhi.retailmanagementsystem.business.order.mapper.OrdersMapper;
import com.yunzhi.retailmanagementsystem.business.order.model.po.OrderGood;
import com.yunzhi.retailmanagementsystem.business.order.model.po.Orders;
import com.yunzhi.retailmanagementsystem.business.report.mapper.ReportsMapper;
import com.yunzhi.retailmanagementsystem.business.report.model.dto.*;
import com.yunzhi.retailmanagementsystem.business.report.model.po.Reports;
import com.yunzhi.retailmanagementsystem.business.report.model.vo.*;
import com.yunzhi.retailmanagementsystem.business.report.service.ReportsService;
import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.common.model.response.*;
import com.yunzhi.retailmanagementsystem.common.security.authentication.SecurityUtils;
import com.yunzhi.retailmanagementsystem.common.utils.coverter.VOConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 报表服务实现类
 * 处理销售报表、库存报表、客户报表的生成与查询
 */
@Service
public class ReportsServiceImpl extends ServiceImpl<ReportsMapper, Reports> implements ReportsService {

    @Autowired
    private OrderGoodMapper orderGoodMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private CustomersMapper customersMapper;

    @Autowired
    private ReportsMapper reportsMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(ReportsServiceImpl.class);

    // 其他代码保持不变

    /**
     * 生成销售报表
     * @param request 销售报表请求参数
     * @return 销售报表数据列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SalesReportVO> generateSalesReport(SalesReportRequestDTO request) {
        final String reportId = UUID.randomUUID().toString();
        try {
            // 参数基础校验
            validateSalesRequest(request);

            // 1. 查询指定时间范围内的订单商品记录
            List<OrderGood> orderGoods = querySalesData(request);
            if (orderGoods.isEmpty()) {
                log.warn("[销售报表] 无销售数据 | reportId:{}", reportId);
                return Collections.emptyList();
            }

            // 2. 处理销售数据并生成报表
            List<SalesReportVO> reportData = processSalesData(orderGoods);

            // 3. 持久化报表记录
            saveReport(reportId, request, reportData);

            return reportData;

        } catch (BusinessException e) {
            throw e; // 直接传递已知业务异常
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.REPORT_SERIALIZATION_ERROR,
                    "销售报表序列化失败", e);
        } catch (DataAccessException e) {
            throw new BusinessException(ErrorCode.DB_OPERATION_FAILED,
                    "销售数据查询失败", e);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.REPORT_DATA_EMPTY,
                    "销售报表生成失败", e);
        }
    }

    /**
     * 生成库存报表
     * @param request 库存报表请求参数
     * @return 库存报表数据列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<InventoryReportVO> generateInventoryReport(InventoryReportRequestDTO request) {
        final String reportId = UUID.randomUUID().toString();
        try {
            // 1. 查询所有商品库存数据
            List<Goods> goods = goodsMapper.selectList(null);
            if (goods.isEmpty()) {
                throw new BusinessException(ErrorCode.GOODS_NOT_FOUND, "未找到商品数据");
            }

            // 2. 构建库存报表数据
            List<InventoryReportVO> reportData = processInventoryData(goods, request);

            // 3. 持久化报表记录
            saveReport(reportId, request, reportData);

            return reportData;

        } catch (BusinessException e) {
            throw e;
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.REPORT_SERIALIZATION_ERROR,
                    "库存报表序列化失败", e);
        } catch (DataAccessException e) {
            throw new BusinessException(ErrorCode.DB_OPERATION_FAILED,
                    "库存数据查询失败", e);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.REPORT_GENERATE_FAILED,
                    "库存报表生成失败", e);
        }
    }

    /**
     * 生成客户报表
     * @param request 客户报表请求参数
     * @return 客户报表数据列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CustomerReportVO> generateCustomerReport(CustomerReportRequestDTO request) {
        final String reportId = UUID.randomUUID().toString();
        try {
            // 1. 查询指定时间范围内的订单数据
            List<Orders> orders = queryCustomerOrders(request);
            if (orders.isEmpty()) {
                throw new BusinessException(ErrorCode.ORDER_NOT_FOUND, "未找到相关订单");
            }

            // 2. 处理客户数据并生成报表
            List<CustomerReportVO> reportData = processCustomerData(orders);

            // 3. 持久化报表记录
            saveReport(reportId, request, reportData);

            return reportData;

        } catch (BusinessException e) {
            throw e;
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.REPORT_SERIALIZATION_ERROR,
                    "客户报表序列化失败", e);
        } catch (DataAccessException e) {
            throw new BusinessException(ErrorCode.DB_OPERATION_FAILED,
                    "客户数据查询失败", e);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.REPORT_GENERATE_FAILED,
                    "客户报表生成失败", e);
        }
    }

    /**
     * 分页查询报表记录
     * @param request 分页查询参数
     * @return 分页结果
     */
    @Override
    public PageResponseVO<List<ReportVO>> queryReportsWithPagination(ReportQueryRequest request) {
        try {
            // 1. 构建分页查询条件
            Page<Reports> page = new Page<>(request.getCurrentPage(), request.getPageSize());
            QueryWrapper<Reports> queryWrapper = buildReportQueryWrapper(request);

            // 2. 执行分页查询
            Page<Reports> reportsPage = this.page(page, queryWrapper);

            // 3. 转换查询结果
            return convertToPageResponse(reportsPage);
        } catch (DataAccessException e) {
            throw new BusinessException(ErrorCode.DB_OPERATION_FAILED,
                    "报表查询失败", e);
        }
    }

    // region 私有方法

    /**
     * 保存报表记录到数据库
     * @param reportId 报表唯一ID
     * @param request 原始请求参数
     * @param reportData 报表数据
     */
    private void saveReport(String reportId, Object request, List<?> reportData)
            throws JsonProcessingException {
        try {
            String userId = SecurityUtils.getCurrentUserId();
            String parameters = objectMapper.writeValueAsString(request);
            String content = objectMapper.writeValueAsString(reportData);

            Reports report = new Reports()
                    .setReportId(reportId)
                    .setUserId(userId)
                    .setParameters(parameters)
                    .setGeneratedDate(LocalDateTime.now())
                    .setReportContent(content);
            try {
                save(report);
            } catch (Exception e) {
                log.error("Error : ", e);
                throw new BusinessException(ErrorCode.REPORT_SAVE_FAILED, "报表存储失败");
            }
            log.info("报表保存成功 | reportId:{} | userId:{}", reportId, userId);
        } catch (JsonProcessingException e) {
            log.error("报表参数序列化失败 | reportId:{}", reportId, e);
            throw e;
        }
    }

    /**
     * 处理销售数据生成报表
     */
    private List<SalesReportVO> processSalesData(List<OrderGood> orderGoods) {
        // 按商品ID聚合销售数量
        Map<String, Integer> quantityMap = orderGoods.stream()
                .collect(Collectors.groupingBy(
                        OrderGood::getGoodId,
                        Collectors.summingInt(OrderGood::getQuantity)
                ));

        // 获取所有商品信息
        List<Goods> goodsList = goodsMapper.selectList(null);
        if (goodsList.isEmpty()) {
            throw new BusinessException(ErrorCode.GOODS_NOT_FOUND);
        }

        // 构建报表数据
        return goodsList.stream()
                .map(good -> {
                    Integer quantity = quantityMap.getOrDefault(good.getGoodId(), 0);
                    BigDecimal total = good.getPrice().multiply(BigDecimal.valueOf(quantity));
                    return new SalesReportVO(
                            good.getGoodId(),
                            good.getName(),
                            quantity,
                            total
                    );
                })
                .filter(vo -> vo.getQuantitySold() > 0)
                .collect(Collectors.toList());
    }

    /**
     * 处理库存数据生成报表
     */
    private List<InventoryReportVO> processInventoryData(List<Goods> goods,
                                                         InventoryReportRequestDTO request) {
        return goods.stream()
                .map(good -> new InventoryReportVO(
                        good.getGoodId(),
                        good.getName(),
                        good.getQuantity(),
                        request.getThreshold()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 处理客户数据生成报表
     */
    private List<CustomerReportVO> processCustomerData(List<Orders> orders) {
        // 按客户ID分组
        Map<String, List<Orders>> customerOrdersMap = orders.stream()
                .collect(Collectors.groupingBy(Orders::getCustomerId));

        // 获取客户信息
        Set<String> customerIds = customerOrdersMap.keySet();
        List<Customers> customers = customersMapper.selectBatchIds(customerIds);
        Map<String, Customers> customerMap = customers.stream()
                .collect(Collectors.toMap(Customers::getCustomerId, Function.identity()));

        return customerIds.stream().map(customerId -> {
            Customers customer = customerMap.get(customerId);
            List<Orders> customerOrders = customerOrdersMap.get(customerId);

            // 计算总消费金额
            BigDecimal total = customerOrders.stream()
                    .map(Orders::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 获取最后购买时间
            LocalDateTime lastPurchase = customerOrders.stream()
                    .map(Orders::getOrderDate)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            return new CustomerReportVO(
                    customerId,
                    customer.getName(),
                    customer.getEmail(),
                    customerOrders.size(),
                    total,
                    lastPurchase
            );
        }).collect(Collectors.toList());
    }

    /**
     * 构建报表查询条件
     */
    private QueryWrapper<Reports> buildReportQueryWrapper(ReportQueryRequest request) {
        QueryWrapper<Reports> wrapper = new QueryWrapper<>();
        if (request.getReportId() != null) {
            wrapper.eq("report_id", request.getReportId());
        }
        if (request.getUserId() != null) {
            wrapper.eq("user_id", request.getUserId());
        }
        if (request.getStartTime() != null && request.getEndTime() != null) {
            wrapper.between("generated_date", request.getStartTime(), request.getEndTime());
        }
        return wrapper;
    }

    /**
     * 转换分页结果为响应格式
     */
    private PageResponseVO<List<ReportVO>> convertToPageResponse(Page<Reports> page) {
        List<ReportVO> vos = page.getRecords().stream()
                .map(VOConverter::convertToVO)
                .collect(Collectors.toList());

        PaginationVO pagination = new PaginationVO(
                (int) page.getCurrent(),
                (int) page.getPages(),
                (int) page.getSize(),
                (int) page.getTotal()
        );

        return new PageResponseVO<>(vos, pagination);
    }

    /**
     * 销售报表参数校验
     */
    private void validateSalesRequest(SalesReportRequestDTO request) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "时间范围不能为空");
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "开始日期不能晚于结束日期");
        }
    }

    /**
     * 查询销售数据
     */
    private List<OrderGood> querySalesData(SalesReportRequestDTO request) {
        LocalDateTime start = request.getStartDate().atStartOfDay();
        LocalDateTime end = request.getEndDate().atTime(23, 59, 59);

        // 查询时间范围内的订单ID
        List<String> orderIds = ordersMapper.selectByOrderDateRange(start, end);

        // 根据订单ID查询商品记录
        return orderGoodMapper.selectBatchIds(orderIds);
    }

    /**
     * 查询客户相关订单数据
     */
    private List<Orders> queryCustomerOrders(CustomerReportRequestDTO request) {
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.between("orderDate", request.getStartDate(), request.getEndDate());
        return ordersMapper.selectList(wrapper);
    }
    // endregion
}