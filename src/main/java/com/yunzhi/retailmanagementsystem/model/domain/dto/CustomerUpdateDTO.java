package com.yunzhi.retailmanagementsystem.model.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record CustomerUpdateDTO(
        @Size(min = 2, max = 20)
        String name,

        @Email
        String email,

        @Pattern(regexp = "^1[3-9]\\d{9}$")
        String phone
) {}