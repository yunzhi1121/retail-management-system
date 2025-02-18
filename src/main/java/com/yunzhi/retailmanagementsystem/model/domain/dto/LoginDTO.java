package com.yunzhi.retailmanagementsystem.model.domain.dto;

import javax.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank String username,
        @NotBlank String password
) {}