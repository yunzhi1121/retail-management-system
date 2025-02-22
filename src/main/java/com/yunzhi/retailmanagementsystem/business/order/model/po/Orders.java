package com.yunzhi.retailmanagementsystem.business.order.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName orders
 */
@TableName(value ="orders")
@Data
@Accessors(chain = true)
public class Orders implements Serializable {
    /**
     *  订单ID
     */
    @TableId
    private String orderId;

    /**
     *  客户ID
     */
    private String customerId;

    /**
     *  订单日期
     */
    private LocalDateTime orderDate;

    /**
     * 订单状态
     */
    private String status;

    /**
     *  订单总金额
     */
    private BigDecimal totalAmount;

    /**
     *  支付方式
     */
    private String paymentMethod;

    /**
     * 配送方式
     */
    private String deliveryMethod;

    /**
     *  备注
     */
    private String remarks;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Orders(String string, String customerId, LocalDateTime date, String description, BigDecimal bigDecimal, String paymentMethod, String deliveryMethod, String remarks) {
        this.orderId = string;
        this.customerId = customerId;
        this.orderDate = date;
        this.status = description;
        this.totalAmount = bigDecimal;
        this.paymentMethod = paymentMethod;
        this.deliveryMethod = deliveryMethod;
        this.remarks = remarks;
    }

    public Orders() {

    }


    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Orders other = (Orders) that;
        return (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getCustomerId() == null ? other.getCustomerId() == null : this.getCustomerId().equals(other.getCustomerId()))
            && (this.getOrderDate() == null ? other.getOrderDate() == null : this.getOrderDate().equals(other.getOrderDate()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getTotalAmount() == null ? other.getTotalAmount() == null : this.getTotalAmount().equals(other.getTotalAmount()))
            && (this.getPaymentMethod() == null ? other.getPaymentMethod() == null : this.getPaymentMethod().equals(other.getPaymentMethod()))
            && (this.getDeliveryMethod() == null ? other.getDeliveryMethod() == null : this.getDeliveryMethod().equals(other.getDeliveryMethod()))
            && (this.getRemarks() == null ? other.getRemarks() == null : this.getRemarks().equals(other.getRemarks()));
    }

    /**
     * 重写hashCode方法，以确保对象在哈希集合中能够正确地被识别和存储
     * 此方法根据对象的多个属性计算哈希码，以提高哈希集合操作的效率和准确性
     *
     * @return int 返回计算得到的哈希码
     */
    @Override
    public int hashCode() {
        final int prime = 31; // 使用质数31作为乘法因子，以优化哈希码的计算结果
        int result = 1; // 初始化哈希码结果

        // 以下代码逐个处理对象的属性，使用三元运算符避免因属性为null而引发的NullPointerException
        // 对于每个属性，如果为null，则对该属性的哈希码贡献为0；否则，调用该属性的hashCode方法获取哈希码
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getCustomerId() == null) ? 0 : getCustomerId().hashCode());
        result = prime * result + ((getOrderDate() == null) ? 0 : getOrderDate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getTotalAmount() == null) ? 0 : getTotalAmount().hashCode());
        result = prime * result + ((getPaymentMethod() == null) ? 0 : getPaymentMethod().hashCode());
        result = prime * result + ((getDeliveryMethod() == null) ? 0 : getDeliveryMethod().hashCode());
        result = prime * result + ((getRemarks() == null) ? 0 : getRemarks().hashCode());

        return result; // 返回最终计算得到的哈希码
    }

    /**
     * 重写toString方法，提供对象的字符串表示
     * 该方法包括类名、哈希码、订单ID、客户ID、订单日期、状态、总金额、支付方式、配送方式、备注和序列化版本ID
     *
     * @return 对象的字符串表示
     */
    @Override
    public String toString() {
        // 使用StringBuilder高效地构建字符串
        StringBuilder sb = new StringBuilder();

        // 添加类名
        sb.append(getClass().getSimpleName());
        sb.append(" [");

        // 添加对象的哈希码
        sb.append("Hash = ").append(hashCode());

        // 添加订单ID
        sb.append(", orderID=").append(orderId);

        // 添加客户ID
        sb.append(", customerID=").append(customerId);

        // 添加订单日期
        sb.append(", orderDate=").append(orderDate);

        // 添加状态
        sb.append(", status=").append(status);

        // 添加总金额
        sb.append(", totalAmount=").append(totalAmount);

        // 添加支付方式
        sb.append(", paymentMethod=").append(paymentMethod);

        // 添加配送方式
        sb.append(", deliveryMethod=").append(deliveryMethod);

        // 添加备注
        sb.append(", remarks=").append(remarks);

        // 添加序列化版本ID
        sb.append(", serialVersionUID=").append(serialVersionUID);

        // 结束字符串构建
        sb.append("]");

        // 返回构建好的字符串
        return sb.toString();
    }
}