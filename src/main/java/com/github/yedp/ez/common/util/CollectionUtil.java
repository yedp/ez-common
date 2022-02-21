package com.github.yedp.ez.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class CollectionUtil extends CollectionUtils {
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 多传入的集合元素进行类型转换并返回转换后的类型集合
     *
     * @param list  参数
     * @param clazz 目标类型
     * @param <T>   原对象类型
     * @param <R>   转换后对象类型
     * @return java.util.List
     **/
    public static <T, R> List<R> convertList(List<T> list, Class<R> clazz) {
        return isEmpty(list) ? new ArrayList<>() : convertList(list, clazz, null);
    }

    /**
     * 多传入的集合元素进行类型转换并返回转换后的类型集合,提供了回调函数
     *
     * @param list      参数
     * @param clazz     目标类型
     * @param <T>       原对象类型
     * @param <R>       转换后对象类型
     * @param predicate 回调函数
     * @return java.util.List
     **/
    public static <T, R> List<R> convertList(List<T> list, Class<R> clazz, BiPredicate<T, R> predicate) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.parallelStream()
                .map(t -> {
                    final R r;
                    try {
                        r = clazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e.toString());
                    }
                    BeanUtils.copyProperties(t, r);
                    if (Objects.nonNull(predicate)) {
                        predicate.test(t, r);
                    }

                    return r;
                }).collect(Collectors.toList());
    }
}
