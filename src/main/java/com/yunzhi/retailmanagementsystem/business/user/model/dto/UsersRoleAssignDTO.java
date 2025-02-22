package com.yunzhi.retailmanagementsystem.business.user.model.dto;

import javax.validation.constraints.NotBlank;

public record UsersRoleAssignDTO(
        @NotBlank String role
) {}