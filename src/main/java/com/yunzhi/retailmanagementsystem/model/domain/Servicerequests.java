package com.yunzhi.retailmanagementsystem.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName servicerequests
 */
@TableName(value ="servicerequests")
@Data
public class Servicerequests implements Serializable {
    /**
     * 
     */
    @TableId
    private String requestID;

    /**
     * 
     */
    private String customerID;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String status;

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
        Servicerequests other = (Servicerequests) that;
        return (this.getRequestID() == null ? other.getRequestID() == null : this.getRequestID().equals(other.getRequestID()))
            && (this.getCustomerID() == null ? other.getCustomerID() == null : this.getCustomerID().equals(other.getCustomerID()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRequestID() == null) ? 0 : getRequestID().hashCode());
        result = prime * result + ((getCustomerID() == null) ? 0 : getCustomerID().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", requestID=").append(requestID);
        sb.append(", customerID=").append(customerID);
        sb.append(", description=").append(description);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}