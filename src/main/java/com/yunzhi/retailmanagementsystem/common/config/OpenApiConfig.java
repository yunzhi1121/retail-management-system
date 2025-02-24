package com.yunzhi.retailmanagementsystem.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
               .info(new Info()
                       .title("物流进销存管理系统 API 文档")
                       .description("该文档包含物流进销存管理的所有 API 信息")
                       .version("1.0.0"));
    }
}