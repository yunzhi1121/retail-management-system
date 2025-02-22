package com.yunzhi.retailmanagementsystem.common.security.authentication;

import com.yunzhi.retailmanagementsystem.common.exception.BusinessException;
import com.yunzhi.retailmanagementsystem.common.model.response.ErrorCode;
import com.yunzhi.retailmanagementsystem.common.utils.crypto.RSAKeyUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@Component
public class JwtTokenProvider {

    @Value("${jwt.expiration:3600000}")
    private long expirationTime;  // 默认1小时

    // 生成令牌
    public String generateToken(String userId, String role) {
        try {
            PrivateKey privateKey = RSAKeyUtil.getPrivateKeyFromEnv("PRIVATE_KEY");
            
            return Jwts.builder()
                    .setSubject(userId)
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "令牌生成失败：" + e.getMessage());
        }
    }

    // 验证令牌
    public boolean validateToken(String token) {
        try {
            PublicKey publicKey = RSAKeyUtil.getPublicKeyFromEnv("PUBLIC_KEY");
            Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        } catch (SecurityException | MalformedJwtException ex) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID, "令牌无效", ex);
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID, "令牌解析失败", ex);
        }
    }

    // 获取认证信息
    public Authentication getAuthentication(String token){
        try {
            PublicKey publicKey = RSAKeyUtil.getPublicKeyFromEnv("PUBLIC_KEY");
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            // 创建权限集合（需要GrantedAuthority实现）
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role)); // 注意角色前缀

            // 使用用户ID作为principal主体
            return new UsernamePasswordAuthenticationToken(
                    userId, // 关键修改点：直接使用用户ID作为主体
                    null,
                    authorities
            );
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID, "认证信息解析失败");
        }
    }

    // 从令牌中获取用户ID
    public String getUserIdFromToken(String token) {
        try {
            PublicKey publicKey = RSAKeyUtil.getPublicKeyFromEnv("PUBLIC_KEY");
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException ex) {
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        } catch (MalformedJwtException | SecurityException ex) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID, "令牌无效", ex);
        } catch (BusinessException ex) {
            throw ex; // 传递已有业务异常
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID, "令牌解析失败：" + ex.getMessage());
        }
    }
}