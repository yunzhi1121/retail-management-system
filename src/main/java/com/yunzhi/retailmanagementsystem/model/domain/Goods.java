package com.yunzhi.retailmanagementsystem.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName goods
 */
@TableName(value ="goods")
@Data
public class Goods implements Serializable {
    /**
     * 商品ID，唯一标识一个商品
     */
    @TableId
    private String goodID;

    /**
     * 商品名称，用于展示商品的信息
     */
    private String name;

    /**
     * 商品描述，详细介绍了商品的特性、使用方法等信息
     */
    private String description;

    /**
     * 商品数量，表示库存或者销售数量
     */
    private Integer quantity;

    /**
     * 商品价格，用于展示和计算商品的价值
     */
    private BigDecimal price;

    /**
     * 序列化ID，用于在序列化和反序列化过程中唯一标识此类的版本
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 计算商品总价
     * @return 商品总价
     */

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
        Goods other = (Goods) that;
        return (this.getGoodID() == null ? other.getGoodID() == null : this.getGoodID().equals(other.getGoodID()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()))
            && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGoodID() == null) ? 0 : getGoodID().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getQuantity() == null) ? 0 : getQuantity().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", goodID=").append(goodID);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", quantity=").append(quantity);
        sb.append(", price=").append(price);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}