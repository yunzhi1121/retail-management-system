package com.yunzhi.retailmanagementsystem.model.domain.dto;

import javax.validation.constraints.NotBlank;

public record RoleAssignDTO(
        @NotBlank String role
) {}