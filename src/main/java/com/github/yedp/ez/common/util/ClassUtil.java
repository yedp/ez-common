package com.github.yedp.ez.common.util;

import com.github.yedp.ez.common.model.resp.QyWxGroupMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

public class ClassUtil {
    private final static Logger log = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 反射赋值
     *
     * @param clazz
     * @param t
     * @param propertyName
     * @param value
     * @param <T>
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> void invoke(Class<T> clazz, T t, String propertyName, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        propertyName = StringUtils.lineToHump(propertyName);    // 属性名,驼峰
        String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        Field field = ClassUtil.getClassField(clazz, propertyName);    //获取和map的key匹配的属性名称
        if (field == null) {
            return;
        }
        Class<?> fieldTypeClass = field.getType();
        value = convertValType(value, fieldTypeClass);
        clazz.getMethod(setMethodName, field.getType()).invoke(t, value);
    }

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
    public static Object convertValType(Object value, Class<?> fieldTypeClass) {
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

    /**
     * 将map的value值转为实体类中字段类型匹配的方法
     *
     * @param value
     * @param fieldTypeClass
     * @return
     */
    public static String toString(Object value, Class<?> fieldTypeClass) {
        String retVal = null;
        if (Date.class.getName().equals(fieldTypeClass.getName())) {
            retVal = DateUtils.format((Date) value);
        } else {
            retVal = value.toString();
        }
        return retVal;
    }

    /**
     * 获取类的声明熟悉值
     *
     * @param clazz 类
     * @return 字段名称
     */
    public static List<String> getFieldNameList(Class<?> clazz) {
        return getFieldNameList(clazz, null);
    }

    /**
     * 获取类的声明熟悉值
     *
     * @param clazz        类
     * @param skipFieldSet 需要跳过属性值
     * @return 字段名称
     */
    public static List<String> getFieldNameList(Class<?> clazz, Set<String> skipFieldSet) {
        List<String> fieldList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (skipFieldSet != null && skipFieldSet.contains(fields[i].getName())) {
                continue;
            }
            fieldList.add(fields[i].getName());
        }
        return fieldList;
    }

    public static <T> List<String> getFieldValueList(List<String> fieldNameList, Class<T> clazz, T t) {
        List<String> valueList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            //有的字段是用private修饰的 将他设置为可读
            fields[i].setAccessible(true);
            try {
                map.put(fields[i].getName(), toString(fields[i].get(t), fields[i].getType()));
            } catch (Exception e) {
                log.error("getFieldValueList.error: fieldName:{}; error:{}", fields[i].getName(), e);
            }
        }
        for (String fieldName : fieldNameList) {
            valueList.add(map.get(fieldName));
        }
        return valueList;
    }
}
