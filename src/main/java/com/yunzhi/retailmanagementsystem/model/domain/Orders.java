package com.yunzhi.retailmanagementsystem.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders implements Serializable {
    /**
     * 
     */
    @TableId
    private String orderID;

    /**
     * 
     */
    private String customerID;

    /**
     * 
     */
    private Date orderDate;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private BigDecimal totalAmount;

    /**
     * 
     */
    private String paymentMethod;

    /**
     * 
     */
    private String deliveryMethod;

    /**
     * 
     */
    private String remarks;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
        return (this.getOrderID() == null ? other.getOrderID() == null : this.getOrderID().equals(other.getOrderID()))
            && (this.getCustomerID() == null ? other.getCustomerID() == null : this.getCustomerID().equals(other.getCustomerID()))
            && (this.getOrderDate() == null ? other.getOrderDate() == null : this.getOrderDate().equals(other.getOrderDate()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getTotalAmount() == null ? other.getTotalAmount() == null : this.getTotalAmount().equals(other.getTotalAmount()))
            && (this.getPaymentMethod() == null ? other.getPaymentMethod() == null : this.getPaymentMethod().equals(other.getPaymentMethod()))
            && (this.getDeliveryMethod() == null ? other.getDeliveryMethod() == null : this.getDeliveryMethod().equals(other.getDeliveryMethod()))
            && (this.getRemarks() == null ? other.getRemarks() == null : this.getRemarks().equals(other.getRemarks()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOrderID() == null) ? 0 : getOrderID().hashCode());
        result = prime * result + ((getCustomerID() == null) ? 0 : getCustomerID().hashCode());
        result = prime * result + ((getOrderDate() == null) ? 0 : getOrderDate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getTotalAmount() == null) ? 0 : getTotalAmount().hashCode());
        result = prime * result + ((getPaymentMethod() == null) ? 0 : getPaymentMethod().hashCode());
        result = prime * result + ((getDeliveryMethod() == null) ? 0 : getDeliveryMethod().hashCode());
        result = prime * result + ((getRemarks() == null) ? 0 : getRemarks().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", orderID=").append(orderID);
        sb.append(", customerID=").append(customerID);
        sb.append(", orderDate=").append(orderDate);
        sb.append(", status=").append(status);
        sb.append(", totalAmount=").append(totalAmount);
        sb.append(", paymentMethod=").append(paymentMethod);
        sb.append(", deliveryMethod=").append(deliveryMethod);
        sb.append(", remarks=").append(remarks);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}