package com.yunzhi.retailmanagementsystem.business.user.model.dto;

import javax.validation.constraints.NotBlank;

public class UsersUpdateUsernameDTO {
    @NotBlank(message = "新用户名不能为空")
    private String newUsername;

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }
}