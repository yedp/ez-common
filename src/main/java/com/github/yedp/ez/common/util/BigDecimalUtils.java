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

}