package com.yunzhi.retailmanagementsystem.model.domain.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public record OrderUpdateDTO(
        @NotEmpty(message = "商品条目不能为空")
        List<OrderItemDTO> items,

        @NotBlank(message = "支付方式不能为空")
        String paymentMethod,

        @NotBlank(message = "配送方式不能为空")
        String deliveryMethod,

        String remarks
) {}