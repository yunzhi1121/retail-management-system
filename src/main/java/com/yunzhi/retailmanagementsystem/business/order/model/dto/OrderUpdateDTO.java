package com.yunzhi.retailmanagementsystem.business.order.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OrderUpdateDTO {

    @NotEmpty(message = "商品条目不能为空")
    private List<OrderItemDTO> items;

    @NotBlank(message = "支付方式不能为空")
    private String paymentMethod;

    @NotBlank(message = "配送方式不能为空")
    private String deliveryMethod;

    private String remarks;
}
