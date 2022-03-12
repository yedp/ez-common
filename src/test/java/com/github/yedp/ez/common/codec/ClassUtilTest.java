package com.github.yedp.ez.common.codec;

import com.github.yedp.ez.common.util.ClassUtil;
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

        System.out.println("int1 instance Integer:" + ClassUtil.isObjectInstanceClass(int1, Integer.class));
        System.out.println("string1 instance String:" + ClassUtil.isObjectInstanceClass(string1, String.class));
        System.out.println("integer1 instance Integer:" + ClassUtil.isObjectInstanceClass(integer1, Integer.class));
        System.out.println("test instance ClassUtilTest:" + ClassUtil.isObjectInstanceClass(test, ClassUtilTest.class));

        System.out.println("int1 instance String:" + ClassUtil.isObjectInstanceClass(int1, String.class));
        System.out.println("string1 instance Integer:" + ClassUtil.isObjectInstanceClass(string1, Integer.class));
        System.out.println("integer1 instance String:" + ClassUtil.isObjectInstanceClass(integer1, String.class));
        System.out.println("test instance ClassUtil:" + ClassUtil.isObjectInstanceClass(test, ClassUtil.class));

    }
}
