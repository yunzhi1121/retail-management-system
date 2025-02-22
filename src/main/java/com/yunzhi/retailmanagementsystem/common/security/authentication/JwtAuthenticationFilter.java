package com.yunzhi.retailmanagementsystem.common.security.authentication;

import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器，用于解析请求中的JWT令牌并进行验证
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 解析请求中的令牌
        String token = resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = null;
            try {
                // 根据令牌获取认证信息
                auth = jwtTokenProvider.getAuthentication(token);
            } catch (BusinessException ex) {
                throw ex; // 传递已有业务异常
            } catch (Exception e) {
                // 处理认证信息解析失败的情况
                throw new BusinessException(ErrorCode.TOKEN_INVALID, "认证信息解析失败");
            }
            // 将认证信息设置到安全上下文中
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        // 继续执行过滤链
        filterChain.doFilter(request, response);
    }

    /**
     * 从HTTP请求中解析令牌
     *
     * @param req HTTP请求对象
     * @return 解析出的令牌字符串，如果没有提供则返回null
     */
    private String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
