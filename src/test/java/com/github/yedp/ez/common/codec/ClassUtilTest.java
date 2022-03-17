package com.github.yedp.ez.common.codec;

import com.github.yedp.ez.common.util.BeanUtils;
import org.junit.Test;

/**
 * @author yedp
 * @date 2022/3/129:57
 * @comment
 **/
public class ClassUtilTest {
    @Test
    public void testIsObjectInstanceClass() {
        int int1 = 1;
        Integer integer1 = new Integer(1);
        String string1 = "s1";
        ClassUtilTest test = new ClassUtilTest();

        System.out.println("int1 instance Integer:" + BeanUtils.isObjectInstanceClass(int1, Integer.class));
        System.out.println("string1 instance String:" + BeanUtils.isObjectInstanceClass(string1, String.class));
        System.out.println("integer1 instance Integer:" + BeanUtils.isObjectInstanceClass(integer1, Integer.class));
        System.out.println("test instance ClassUtilTest:" + BeanUtils.isObjectInstanceClass(test, ClassUtilTest.class));

        System.out.println("int1 instance String:" + BeanUtils.isObjectInstanceClass(int1, String.class));
        System.out.println("string1 instance Integer:" + BeanUtils.isObjectInstanceClass(string1, Integer.class));
        System.out.println("integer1 instance String:" + BeanUtils.isObjectInstanceClass(integer1, String.class));
        System.out.println("test instance ClassUtil:" + BeanUtils.isObjectInstanceClass(test, BeanUtils.class));

    }
}
