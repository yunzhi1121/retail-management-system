package com.yunzhi.retailmanagementsystem.common.utils.crypto;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
public class RSAKeyGenerator {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // 指定密钥长度
        KeyPair pair = keyGen.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        // 保存私钥
        try (FileOutputStream fos = new FileOutputStream("private_key.pem")) {
            fos.write(privateKey.getEncoded());
        }

        // 保存公钥
        try (FileOutputStream fos = new FileOutputStream("public_key.pem")) {
            fos.write(publicKey.getEncoded());
        }

        System.out.println("RSA 密钥对生成完成！");
    }
}
