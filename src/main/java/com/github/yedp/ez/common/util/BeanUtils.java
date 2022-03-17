package com.github.yedp.ez.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

public class BeanUtils {
    private final static Logger log = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 反射赋值
     *
     * @param clazz        对象类
     * @param t            对象
     * @param propertyName 字段名称
     * @param value        字段值
     * @param <T>          对象类型
     * @throws NoSuchMethodException     异常
     * @throws InvocationTargetException 异常
     * @throws IllegalAccessException    异常
     */
    public static <T> void invoke(Class<T> clazz, T t, String propertyName, Object value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (propertyName.indexOf("_") > 0) {
            propertyName = StringUtils.lineToHump(propertyName);    // 属性名,驼峰
        }
        String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        Field field = BeanUtils.getClassField(clazz, propertyName);    //获取和map的key匹配的属性名称
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
     * @return 返回字段
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
     * @param value          值
     * @param fieldTypeClass 字段类型
     * @return 转换后的值
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
     * @param value          值
     * @param fieldTypeClass 类型
     * @return 转换后的值
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

    /**
     * 判断一个对象是否是某个类型
     *
     * @param object 对象
     * @param clazz  类型
     * @return 匹配结果
     */
    public static boolean isObjectInstanceClass(Object object, Class clazz) {
        if (object == null || clazz == null) {
            return false;
        }
        if (object.getClass().equals(clazz)) {
            return true;
        }
        return false;
    }

    /**
     * 初始化对象属性值为null的值.
     *
     * @param obj 对象
     */
    public static void setNullFieldToDefaultValue(Object obj) {

        if (null == obj) {
            return;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        Class cla = obj.getClass();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    if (null == field.get(obj)) {
                        setFieldDefaultValue(obj, cla, field);
                    }
                } catch (Exception e) {
                    log.error("notNull", JsonUtil.toJsonString(obj));
                    log.error("notNull", e);
                }
                field.setAccessible(false);
            }
        }
    }

    /**
     * 设置对象字段默认值
     *
     * @param obj   对象
     * @param cla   类
     * @param field 字段
     * @throws Exception 异常
     */
    private static void setFieldDefaultValue(Object obj, Class cla, Field field)
            throws Exception {
        Class type = field.getType();
        String fieldName = field.getName();
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method;
        if (String.class.equals(type)) {
            method = cla.getMethod(methodName, String.class);
            method.invoke(obj, "");
        } else if (char.class.equals(type)) {
            method = cla.getMethod(methodName, char.class);
            method.invoke(obj, ' ');
        } else if (Character.class.equals(type)) {
            method = cla.getMethod(methodName, Character.class);
            method.invoke(obj, ' ');
        } else if (boolean.class.equals(type)) {
            method = cla.getMethod(methodName, boolean.class);
            method.invoke(obj, false);
        } else if (Boolean.class.equals(type)) {
            method = cla.getMethod(methodName, Boolean.class);
            method.invoke(obj, false);
        } else if (byte.class.equals(type)) {
            method = cla.getMethod(methodName, byte.class);
            method.invoke(obj, (byte) 0);
        } else if (Byte.class.equals(type)) {
            method = cla.getMethod(methodName, Byte.class);
            method.invoke(obj, (byte) 0);
        } else if (short.class.equals(type)) {
            method = cla.getMethod(methodName, short.class);
            method.invoke(obj, (short) 0);
        } else if (Short.class.equals(type)) {
            method = cla.getMethod(methodName, Short.class);
            method.invoke(obj, (short) 0);
        } else if (int.class.equals(type)) {
            method = cla.getMethod(methodName, int.class);
            method.invoke(obj, 0);
        } else if (Integer.class.equals(type)) {
            method = cla.getMethod(methodName, Integer.class);
            method.invoke(obj, 0);
        } else if (long.class.equals(type)) {
            method = cla.getMethod(methodName, long.class);
            method.invoke(obj, 0);
        } else if (Long.class.equals(type)) {
            method = cla.getMethod(methodName, Long.class);
            method.invoke(obj, 0L);
        } else if (float.class.equals(type)) {
            method = cla.getMethod(methodName, float.class);
            method.invoke(obj, 0.0f);
        } else if (Float.class.equals(type)) {
            method = cla.getMethod(methodName, Float.class);
            method.invoke(obj, 0.0f);
        } else if (double.class.equals(type)) {
            method = cla.getMethod(methodName, double.class);
            method.invoke(obj, 0.0d);
        } else if (Double.class.equals(type)) {
            method = cla.getMethod(methodName, Double.class);
            method.invoke(obj, 0.0d);
        } else if (Date.class.equals(type)) {
            method = cla.getMethod(methodName, Date.class);
            method.invoke(obj, DateUtils.getStartDate());
        } else if (BigDecimal.class.equals(type)) {
            method = cla.getMethod(methodName, BigDecimal.class);
            method.invoke(obj, new BigDecimal(0.0D));
        }
    }

    /**
     * 复制属性
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }
}
