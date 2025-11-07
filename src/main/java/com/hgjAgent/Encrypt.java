package com.hgjAgent;


import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Encrypt {

    /**
     * 用公钥加密明文（密码）
     * @param plainText 要加密的明文（如密码）
     * @return 加密后的密文（Base64编码，便于传输/存储）
     * @throws Exception 加密过程中的异常（公钥无效、明文过长等）
     */
    public static String encryptWithPublicKey(String plainText) throws Exception {
        // 1. 将Base64编码的公钥字符串解码为字节数组
        String publicKeyBase64="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYmW6DpAheRe4Vf8ZqgGaGEHa4ZhfWzsdr+/IDaw0cvz7zHEZiclIh9lFsPbTueLW/H54pyb47ggOvKh2QzhbxefP4eEcaaOuBAU2SxlwyhjI64lnvt+XBmoSvye3QDjaF8D8qG9CGEwBlgPfxHBq0latB9u+3oppijMdai67UswIDAQAB";
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

        // 2. 解析公钥（RSA公钥通常是X.509格式）
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // 3. 初始化加密器（算法+填充方式，需与公钥生成时一致，通常为RSA/ECB/PKCS1Padding）
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 4. 加密明文（注意：RSA加密有长度限制，1024位公钥最多加密117字节明文）
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // 5. 加密结果转为Base64字符串返回
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(encryptWithPublicKey("Q13817759419Q"));
    }

}
