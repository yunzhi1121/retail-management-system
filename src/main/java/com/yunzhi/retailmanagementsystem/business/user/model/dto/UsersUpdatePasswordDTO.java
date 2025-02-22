package com.yunzhi.retailmanagementsystem.business.user.model.dto;

import javax.validation.constraints.NotBlank;

public class UsersUpdatePasswordDTO {
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    public String getNewPassword() {
        return newPassword;
    }
    public String getOldPassword() {
        return oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}