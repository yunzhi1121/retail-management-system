package com.yunzhi.retailmanagementsystem.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
public class RSAKeyUtil {
    // 从环境变量中获取私钥，并将其转换为 PrivateKey 对象
    public static PrivateKey getPrivateKeyFromEnv(String envVarName) throws Exception {
        // 从环境变量中读取 Base64 编码的私钥
        String base64Key = System.getenv(envVarName);
        if (base64Key == null || base64Key.isEmpty()) {
            throw new IllegalArgumentException("Private key not found in environment variables");
        }

        // 解码 Base64 编码的私钥
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);

    }

    // 从环境变量中获取公钥，并将其转换为 PublicKey 对象
    public static PublicKey getPublicKeyFromEnv(String envVarName) throws Exception {
        // 从环境变量中读取 Base64 编码的公钥
        String base64Key = System.getenv(envVarName);
        if (base64Key == null || base64Key.isEmpty()) {
            throw new IllegalArgumentException("Public key not found in environment variables");
        }

        // 解码 Base64 编码的公钥
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
