package com.github.yedp.ez.common.codec;

import com.github.yedp.ez.common.model.resp.QyWxGroupMsg;
import com.github.yedp.ez.common.util.ClassUtil;
import com.github.yedp.ez.common.util.CsvUtils;
import com.github.yedp.ez.common.util.JsonUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CsvUtilTest {
    private final static Logger log = LoggerFactory.getLogger(CsvUtilTest.class);

    static String filePath = "D:/qy_wx_group_msg.csv";

    public static void main(String args[]) throws IOException {
        List<String> list = ClassUtil.getFieldNameList(QyWxGroupMsg.class);
        System.out.println(JsonUtil.toJsonString(list));
        Set<String> set = new HashSet<>();
        set.add("serialVersionUID");
        list = ClassUtil.getFieldNameList(QyWxGroupMsg.class, set);
        System.out.println(JsonUtil.toJsonString(list));
        List<QyWxGroupMsg> listV = CsvUtils.read(new File(filePath), QyWxGroupMsg.class);
        QyWxGroupMsg qyWxGroupMsg = listV.get(0);
        list = ClassUtil.getFieldValueList(ClassUtil.getFieldNameList(QyWxGroupMsg.class),QyWxGroupMsg.class,qyWxGroupMsg);
        System.out.println(JsonUtil.toJsonString(list));

    }


    @Test
    public void testFile() {
        try {
            List<QyWxGroupMsg> list = CsvUtils.read(new File(filePath), QyWxGroupMsg.class);
            log.info("list: {}", JsonUtil.toJsonString(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void export() throws IOException {
        List<QyWxGroupMsg> list = CsvUtils.read(new File(filePath), QyWxGroupMsg.class);
        QyWxGroupMsg qyWxGroupMsg = list.get(0);
        Field[] fields = QyWxGroupMsg.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            //有的字段是用private修饰的 将他设置为可读
            fields[i].setAccessible(true);
            try {
                // 输出属性名和属性值
                System.out.println("属性名" + fields[i].getName() + "-----属性值" + fields[i].get(qyWxGroupMsg));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
