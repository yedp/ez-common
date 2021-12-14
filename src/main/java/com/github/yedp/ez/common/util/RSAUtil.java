package com.github.yedp.ez.common.util;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yedp
 * Date: 2018/12/29 14:33
 * 签名SHA1实现
 */
public class RSAUtil {
    private final static String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private final static String KEY_ALGORITHM = "RSA";

    private final static int MAX_ENCRYPT_BLOCK = 117;
    private final static int MAX_DECRYPT_BLOCK = 128;

    /**
     * 加密（用公钥）
     *
     * @param data      数据（字符串）
     * @param publicKey 公钥
     * @return 加密字符串
     * @throws Exception 加密异常
     */
    public static String encrypt(String data, String publicKey) throws Exception {
        return Base64.encodeBase64String(encrypt(data.getBytes(StandardCharsets.UTF_8), Base64.decodeBase64(publicKey)));
    }

    /**
     * 加密（用公钥）
     *
     * @param data      数据（字节数组）
     * @param publicKey 公钥
     * @return 加密字符串
     * @throws Exception 加密异常
     */
    public static byte[] encrypt(byte[] data, byte[] publicKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(1, publicK);
        return getDoFinalData(data, cipher, MAX_ENCRYPT_BLOCK);
    }

    /**
     * 解密
     *
     * @param encryptData 数据（字符串）
     * @param privateKey  私钥
     * @return 解密数据
     * @throws Exception 解密异常
     */
    public static String decrypt(String encryptData, String privateKey) throws Exception {
        return new String(decrypt(Base64.decodeBase64(encryptData), Base64.decodeBase64(privateKey)), StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param encryptedData 数据（字节数组）
     * @param privateKey    私钥
     * @return 解密数据
     * @throws Exception 解密异常
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(2, privateK);
        return getDoFinalData(encryptedData, cipher, MAX_DECRYPT_BLOCK);
    }


    private static byte[] getDoFinalData(byte[] data, Cipher cipher, int maxBlock) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            int offSet = 0;
            for (int i = 0; inputLen - offSet > 0; offSet = i * maxBlock) {
                byte[] cache;
                if (inputLen - offSet > maxBlock) {
                    cache = cipher.doFinal(data, offSet, maxBlock);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                ++i;
            }
            out.flush();
            byte[] encryptedData = out.toByteArray();
            return encryptedData;
        } finally {
            out.close();
        }
    }


    /**
     * 签名
     *
     * @param param      数据
     * @param privateKey 私钥
     * @return 返回签名数据
     * @throws Exception 签名异常
     */
    public static String sign(String param, String privateKey) throws Exception {
        byte[] data = param.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        byte[] bytes = sign(data, keyBytes);
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 签名
     *
     * @param param      数据（字节数组）
     * @param privateKey 私钥
     * @return 返回签名数据
     * @throws Exception 签名异常
     */
    public static byte[] sign(byte[] param, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(param);
        byte[] bytes = signature.sign();
        return bytes;
    }

    /**
     * 验证签名
     *
     * @param param        参数
     * @param signatureStr 签名字符串
     * @param publicKey    公钥
     * @return 返回验签结果
     * @throws Exception 验签异常
     */
    public static boolean verify(String param, String signatureStr, String publicKey) throws Exception {
        byte[] data = param.getBytes(StandardCharsets.UTF_8);
        byte[] signatureData = java.util.Base64.getDecoder().decode(signatureStr);
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        return verify(data, signatureData, keyBytes);
    }

    /**
     * 验证签名
     *
     * @param param         参数
     * @param signatureData 签名字节
     * @param publicKey     公钥
     * @return 返回验签结果
     * @throws Exception 验签异常
     */
    public static boolean verify(byte[] param, byte[] signatureData, byte[] publicKey) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(param);
        return signature.verify(signatureData);
    }

    /**
     * 生成公私钥对
     *
     * @return 公私钥
     * @throws NoSuchAlgorithmException 异常
     */
    public static Map<String, String> genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        Map<String, String> keyMap = new HashMap<>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey
                .getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey
                .getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put("publicKey", publicKeyString); // 0表示公钥
        keyMap.put("privateKey", privateKeyString); // 1表示私钥
        return keyMap;
    }
}
