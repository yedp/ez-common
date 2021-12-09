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
        if (propertyName.indexOf("_") > 0) {
            propertyName = StringUtils.lineToHump(propertyName);    // 属性名,驼峰
        }
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
        Set<String> skipFieldSet = new HashSet<>();
        skipFieldSet.add("serialVersionUID");
        return getFieldNameList(clazz, skipFieldSet);
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

    /**
     * 获取数据字段列表和数据列表
     *
     * @param clazz 对象类
     * @param tList 对象数据列表
     * @param <T>   对象类型
     * @return 数据列表
     */
    public static <T> FieldDataInfo getFieldValueList(Class<T> clazz, List<T> tList) {
        Set<String> skipFieldSet = new HashSet<>();
        skipFieldSet.add("serialVersionUID");
        return getFieldValueList(clazz, tList, skipFieldSet);
    }

    /**
     * 获取数据字段列表和数据列表
     *
     * @param clazz        对象类
     * @param tList        对象数据列表
     * @param skipFieldSet 需跳过字段
     * @param <T>          对象类型
     * @return 数据列表
     */
    public static <T> FieldDataInfo getFieldValueList(Class<T> clazz, List<T> tList, Set<String> skipFieldSet) {
        FieldDataInfo dataList = new FieldDataInfo();
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldNameList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (skipFieldSet != null && skipFieldSet.contains(fields[i].getName())) {
                continue;
            }
            fieldNameList.add(fields[i].getName());
        }
        dataList.setFieldNameList(fieldNameList);
        List<List<String>> fieldValueList = new ArrayList<>();
        for (T t : tList) {
            List<String> valueList = new ArrayList<>();
            for (int i = 0; i < fields.length; i++) {
                //有的字段是用private修饰的 将他设置为可读
                fields[i].setAccessible(true);
                if (skipFieldSet != null && skipFieldSet.contains(fields[i].getName())) {
                    continue;
                }
                String value = StringUtils.EMPTY;
                try {
                    value = toString(fields[i].get(t), fields[i].getType());
                } catch (Exception e) {
                    log.error("getFieldValueList.toString.error fieldName:{}; error:{}", fields[i].getName(), e);
                }
                valueList.add(value);
            }
            fieldValueList.add(valueList);
        }
        dataList.setFieldValueList(fieldValueList);
        return dataList;
    }

    /**
     * 数据列表，用于csv导出用
     */
    public static class FieldDataInfo {
        /**
         * 字段列表
         */
        private List<String> fieldNameList;
        /**
         * 对象值列表
         */
        private List<List<String>> fieldValueList;

        public List<String> getFieldNameList() {
            return fieldNameList;
        }

        public void setFieldNameList(List<String> fieldNameList) {
            this.fieldNameList = fieldNameList;
        }

        public List<List<String>> getFieldValueList() {
            return fieldValueList;
        }

        public void setFieldValueList(List<List<String>> fieldValueList) {
            this.fieldValueList = fieldValueList;
        }
    }
}
