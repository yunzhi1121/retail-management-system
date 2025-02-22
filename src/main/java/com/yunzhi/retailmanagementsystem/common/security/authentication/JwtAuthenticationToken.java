package com.yunzhi.retailmanagementsystem.common.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

/**
 * 实现Authentication接口的JWT认证令牌类
 * 用于Spring Security框架，表示经过JWT验证的用户身份
 */
public class JwtAuthenticationToken implements Authentication {
    // 用户ID，即JWT令牌中标识用户的部分
    private final String principal;
    // 用户权限列表，表示用户拥有的权限或角色
    private final Collection<? extends GrantedAuthority> authorities;
    // 认证状态，初始化为true，表示用户持有JWT即视为已认证
    private boolean authenticated;

    /**
     * 构造函数
     *
     * @param principal    用户ID
     * @param authorities  用户权限列表
     */
    public JwtAuthenticationToken(String principal,
                                 Collection<? extends GrantedAuthority> authorities) {
        this.principal = principal;
        this.authorities = authorities;
        this.authenticated = true; // 标记为已认证
    }

    //━━━━━━━━━━━━━━ 实现接口方法 ━━━━━━━━━━━━━━

    /**
     * 获取用户权限列表
     *
     * @return 用户权限列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 获取用户凭证信息
     * 在JWT中，凭证信息为空，因为JWT本身即为凭证
     *
     * @return null，因为JWT不需要额外的凭证信息
     */
    @Override
    public Object getCredentials() {
        return null; // JWT不需要证书
    }

    /**
     * 获取用户详细信息
     * 在JWT中，此方法返回null，因为不需要额外的用户细节信息
     *
     * @return null，不需要额外细节
     */
    @Override
    public Object getDetails() {
        return null; // 不需要额外细节
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    @Override
    public String getPrincipal() {
        return principal; // 返回用户ID
    }

    /**
     * 设置认证状态
     * 在JWT中，认证状态一旦设置不可修改，因此抛出不支持操作异常
     *
     * @param isAuthenticated 认证状态
     * @throws UnsupportedOperationException 如果尝试修改认证状态，则抛出此异常
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        throw new UnsupportedOperationException("认证状态不可修改");
    }

    /**
     * 检查用户是否已认证
     *
     * @return 认证状态，始终返回true
     */
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * 获取用户名称
     * 在JWT中，这通常是指用户ID
     *
     * @return 用户ID
     */
    @Override
    public String getName() {
        return principal;
    }
}
