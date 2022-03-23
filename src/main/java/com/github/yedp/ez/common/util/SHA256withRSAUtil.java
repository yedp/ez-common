package com.github.yedp.ez.common.util;


import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author yedp
 * Date: 2018/12/29 14:33
 * 签名SHA1实现
 */
public class SHA256withRSAUtil {
    private final static String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private final static String KEY_ALGORITHM = "RSA";

    public static String getSignature(String privateKeyStr, String paramStr) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(string2PrivateKey(privateKeyStr));
        sign.update(paramStr.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(sign.sign());
    }

    private static PrivateKey string2PrivateKey(String privateKeyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        return privateK;
    }

    public static boolean verify(String paramStr, String publicKeyStr, String signStr) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(string2PublicKey(publicKeyStr));
        sign.update(paramStr.getBytes(StandardCharsets.UTF_8));
        return sign.verify(Base64.getDecoder().decode(signStr.getBytes(StandardCharsets.UTF_8)));
    }

    private static PublicKey string2PublicKey(String publicKeyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
        return publicK;
    }
}
