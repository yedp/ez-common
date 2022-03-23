package com.github.yedp.ez.common.util;

import java.security.MessageDigest;

public class Sha1Util {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return buf.toString();
    }

    /**
     * 生成签名
     *
     * @param str 参数
     * @return 签名
     */
    public static String sha1(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验证签名是否正确
     *
     * @param param     需要签名的参数
     * @param signature 签名
     * @return 是否匹配
     */
    public static boolean verify(String param, String signature) {
        if (StringUtils.isEmpty(param) || StringUtils.isEmpty(signature)) {
            return false;
        }
        return signature.equals(sha1(param));
    }
}
