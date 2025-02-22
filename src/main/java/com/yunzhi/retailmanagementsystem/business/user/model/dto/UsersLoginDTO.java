package com.yunzhi.retailmanagementsystem.business.user.model.dto;

import javax.validation.constraints.NotBlank;

public record UsersLoginDTO(
        @NotBlank String username,
        @NotBlank String password
) {}