package com.yunzhi.retailmanagementsystem.business.user.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @TableName users
 */
@TableName(value = "users")
@Data
public class Users implements Serializable {
    /**
     *
     */
    @TableId
    private String userId;

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String password;

    /**
     *
     */
    private String role;


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
        if (getClass()!= that.getClass()) {
            return false;
        }
        Users other = (Users) that;
        return (this.getUserId() == null? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getUsername() == null? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
                && (this.getPassword() == null? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
                && (this.getRole() == null? other.getRole() == null : this.getRole().equals(other.getRole()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null)? 0 : getUserId().hashCode());
        result = prime * result + ((getUsername() == null)? 0 : getUsername().hashCode());
        result = prime * result + ((getPassword() == null)? 0 : getPassword().hashCode());
        result = prime * result + ((getRole() == null)? 0 : getRole().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userID=").append(userId);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", role=").append(role);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}