package com.yunzhi.retailmanagementsystem.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName customers
 */
@TableName(value ="customers")
@Data
public class Customers implements Serializable {
    /**
     * 客户ID，唯一标识一个客户
     */
    @TableId
    private String customerID;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 客户邮箱地址
     */
    private String email;

    /**
     * 客户联系电话
     */
    private String phone;

    /**
     * 序列化版本ID，用于序列化过程中的版本控制
     */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    // 省略getter/setter方法
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
        Customers other = (Customers) that;
        return (this.getCustomerID() == null ? other.getCustomerID() == null : this.getCustomerID().equals(other.getCustomerID()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCustomerID() == null) ? 0 : getCustomerID().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", customerID=").append(customerID);
        sb.append(", name=").append(name);
        sb.append(", email=").append(email);
        sb.append(", phone=").append(phone);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }


}