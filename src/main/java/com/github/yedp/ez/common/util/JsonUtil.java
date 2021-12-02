package com.github.yedp.ez.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    // 日起格式化
    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * 对象转Json格式字符串
     *
     * @param obj 对象
     * @param <T> 对象
     * @return 对象字符串
     * @throws JsonProcessingException 异常
     */

    public static <T> String toJsonString(T obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }
        return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
    }

    /**
     * 对象转Json格式字符串(格式化的Json字符串)
     *
     * @param obj 对象
     * @param <T> 对象
     * @return 对象字符串
     * @throws JsonProcessingException 异常
     */
    public static <T> String toJsonStringPretty(T obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }
        return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * 字符串转换为自定义对象
     *
     * @param str   要转换的字符串
     * @param <T>   对象
     * @param clazz 自定义对象的class对象
     * @return 自定义对象
     * @throws IOException 异常
     */
    public static <T> T readValue(String str, Class<T> clazz) throws IOException {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
    }

    /**
     * 字符串转换为自定义对象
     *
     * @param str           要转换的字符串
     * @param typeReference 类型
     * @param <T>           对象
     * @return 对象
     * @throws IOException 异常
     */
    public static <T> T readValue(String str, TypeReference<T> typeReference) throws IOException {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
    }

    /**
     * 字符串转换为自定义对象列表
     *
     * @param str             要转换的字符串
     * @param collectionClazz 集合类型
     * @param elementClazzes  元素类型
     * @param <T>             对象
     * @return 集合对象
     * @throws IOException 异常
     */
    public static <T> T readValue(String str, Class<?> collectionClazz, Class<?>... elementClazzes) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClazz, elementClazzes);
        return objectMapper.readValue(str, javaType);
    }
}