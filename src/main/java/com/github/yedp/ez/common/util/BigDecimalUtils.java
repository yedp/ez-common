package com.github.yedp.ez.common.util;

import java.math.BigDecimal;

public class BigDecimalUtils {
    public static final BigDecimal B100 = new BigDecimal(100);

    public static int getYuanToFen(BigDecimal money) {
        return money.multiply(B100).intValue();
    }

    public static BigDecimal getFenToYuan(int money) {
        return new BigDecimal(money).divide(B100, 2, BigDecimal.ROUND_DOWN);
    }

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor, int scale) {
        return dividend.divide(divisor, scale, BigDecimal.ROUND_DOWN);
    }

    /**
     * 计算折扣（保留小数点后的0）
     *
     * @param salePrice   售价
     * @param originPrice 原价
     * @return
     */
    public static String calculateDiscount(Integer salePrice, Integer originPrice) {
        if(salePrice==null ||originPrice==null){
            return null;
        }
        return new BigDecimal(String.valueOf(salePrice * 10)).divide(new BigDecimal(String.valueOf(originPrice)), 1, BigDecimal.ROUND_DOWN).toString();
    }

}