package com.github.yedp.ez.common.codec;

import com.github.yedp.ez.common.util.RSAUtil;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class RSAUtilTest {
    private final static Logger log = LoggerFactory.getLogger(RSAUtilTest.class);


    private static String publicKey = "";
    private static String privateKey = "";

    @Before
    public void before() throws NoSuchAlgorithmException {
        Map<String, String> keyPair = RSAUtil.genKeyPair();
        privateKey = keyPair.get("privateKey");
        publicKey = keyPair.get("publicKey");
        log.info("publicKey : {}", publicKey);
        log.info("privateKey : {}", privateKey);
    }

    @Test
    public void testSign() throws Exception {
        log.info("publicKey : {}", publicKey);
        log.info("privateKey : {}", privateKey);
        String testData = "Test123c好测试";
        String signStr = RSAUtil.sign(testData, privateKey);
        log.info("signStr : {}", signStr);
        log.info("signRs : {}", RSAUtil.verify(testData, signStr, publicKey));
    }

    @Test
    public void testEncrypt() throws Exception {
        String testData = "Test123c好测试";
        String encryptStr = RSAUtil.encrypt(testData, publicKey);
        log.info("encryptStr : {}", encryptStr);
        log.info("decryptStr : {}", RSAUtil.decrypt(encryptStr, privateKey));
    }
}
