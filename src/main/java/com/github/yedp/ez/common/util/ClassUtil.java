package com.github.yedp.ez.common.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

public class ClassUtil {

    /**
     * 根据给定对象类匹配对象中的特定字段
     *
     * @param clazz     类
     * @param fieldName 字段名称
     * @return
     */
    public static Field getClassField(Class<?> clazz, String fieldName) {
        if (Object.class.getName().equals(clazz.getName())) {
            return null;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        Class<?> superClass = clazz.getSuperclass();    //如果该类还有父类，将父类对象中的字段也取出
        if (superClass != null) {                        //递归获取
            return getClassField(superClass, fieldName);
        }
        return null;
    }

    /**
     * 将map的value值转为实体类中字段类型匹配的方法
     *
     * @param value
     * @param fieldTypeClass
     * @return
     */
    public static Object convertValType(Object value, Class<?> fieldTypeClass)  {
        Object retVal = null;

        if (Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if (Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if (Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if (Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else if (Date.class.getName().equals(fieldTypeClass.getName())) {
            retVal = DateUtils.parse(String.valueOf(value));
        } else if (BigDecimal.class.getName().equals(fieldTypeClass.getName())) {
            retVal = new BigDecimal(String.valueOf(value));
        } else {
            retVal = value;
        }
        return retVal;
    }
}
