package com.github.yedp.ez.common.util;

public class DistanceUtil {

    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离（单位：m）
     */
    public static int getDistance(double lat1, double lng1, double lat2, double lng2) {
        if (lat1 <= 0 || lng1 <= 0 || lat2 <= 0 || lng2 <= 0) {
            return 0;
        }

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return (int) s;
    }

    /**
     * 经纬度接收的为String类型时
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离（单位：m）
     */
    public static int getDistanceByStr(String lat1, String lng1, String lat2, String lng2) {
        double la1 = StringUtils.parseDouble(lat1);
        double ln1 = StringUtils.parseDouble(lng1);
        double la2 = StringUtils.parseDouble(lat2);
        double ln2 = StringUtils.parseDouble(lng2);
        return getDistance(la1, ln1, la2, ln2);
    }
}
