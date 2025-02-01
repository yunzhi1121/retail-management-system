package com.yunzhi.retailmanagementsystem.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName order_good
 */
@TableName(value ="order_good")
@Data
public class OrderGood implements Serializable {
    /**
     * 
     */
    @TableId
    private String orderID;

    /**
     * 
     */
//    @TableId
    private String goodID;

    /**
     * 
     */
    private Integer quantity;

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
        OrderGood other = (OrderGood) that;
        return (this.getOrderID() == null ? other.getOrderID() == null : this.getOrderID().equals(other.getOrderID()))
            && (this.getGoodID() == null ? other.getGoodID() == null : this.getGoodID().equals(other.getGoodID()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOrderID() == null) ? 0 : getOrderID().hashCode());
        result = prime * result + ((getGoodID() == null) ? 0 : getGoodID().hashCode());
        result = prime * result + ((getQuantity() == null) ? 0 : getQuantity().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", orderID=").append(orderID);
        sb.append(", goodID=").append(goodID);
        sb.append(", quantity=").append(quantity);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}