package com.yunzhi.retailmanagementsystem.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tracking
 */
@TableName(value ="tracking")
@Data
public class Tracking implements Serializable {
    /**
     * 
     */
    @TableId
    private String trackingID;

    /**
     * 
     */
    private String orderID;

    /**
     * 
     */
    private String location;

    /**
     * 
     */
    private Date timestamp;

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
        Tracking other = (Tracking) that;
        return (this.getTrackingID() == null ? other.getTrackingID() == null : this.getTrackingID().equals(other.getTrackingID()))
            && (this.getOrderID() == null ? other.getOrderID() == null : this.getOrderID().equals(other.getOrderID()))
            && (this.getLocation() == null ? other.getLocation() == null : this.getLocation().equals(other.getLocation()))
            && (this.getTimestamp() == null ? other.getTimestamp() == null : this.getTimestamp().equals(other.getTimestamp()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTrackingID() == null) ? 0 : getTrackingID().hashCode());
        result = prime * result + ((getOrderID() == null) ? 0 : getOrderID().hashCode());
        result = prime * result + ((getLocation() == null) ? 0 : getLocation().hashCode());
        result = prime * result + ((getTimestamp() == null) ? 0 : getTimestamp().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", trackingID=").append(trackingID);
        sb.append(", orderID=").append(orderID);
        sb.append(", location=").append(location);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}