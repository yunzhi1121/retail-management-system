package com.yunzhi.retailmanagementsystem.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName reports
 */
@TableName(value ="reports")
@Data
public class Reports implements Serializable {
    /**
     * 
     */
    @TableId
    private String reportID;

    /**
     * 
     */
    private String userID;

    /**
     * 
     */
    private String parameters;

    /**
     * 
     */
    private Date generatedDate;

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
        Reports other = (Reports) that;
        return (this.getReportID() == null ? other.getReportID() == null : this.getReportID().equals(other.getReportID()))
            && (this.getUserID() == null ? other.getUserID() == null : this.getUserID().equals(other.getUserID()))
            && (this.getParameters() == null ? other.getParameters() == null : this.getParameters().equals(other.getParameters()))
            && (this.getGeneratedDate() == null ? other.getGeneratedDate() == null : this.getGeneratedDate().equals(other.getGeneratedDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getReportID() == null) ? 0 : getReportID().hashCode());
        result = prime * result + ((getUserID() == null) ? 0 : getUserID().hashCode());
        result = prime * result + ((getParameters() == null) ? 0 : getParameters().hashCode());
        result = prime * result + ((getGeneratedDate() == null) ? 0 : getGeneratedDate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", reportID=").append(reportID);
        sb.append(", userID=").append(userID);
        sb.append(", parameters=").append(parameters);
        sb.append(", generatedDate=").append(generatedDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}