package com.github.yedp.ez.common.codec;

import com.github.yedp.ez.common.model.resp.QyWxGroupMsg;
import com.github.yedp.ez.common.util.CsvUtils;
import com.github.yedp.ez.common.util.JsonUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class CsvUtilTest {
    private final static Logger log = LoggerFactory.getLogger(CsvUtilTest.class);

    static String filePath = "D:/qy_wx_group_msg.csv";

    public static void main(String args[]) throws IOException {
        List<Map<String, String>> mapList = CsvUtils.read(new File(filePath));
        for (Map record : mapList) {
            System.out.println(JsonUtil.toJsonString(record));

        }
    }


    @Test
    public void testFile() {
        try {
            List<QyWxGroupMsg> list = CsvUtils.read(new File(filePath), QyWxGroupMsg.class);
            log.info(JsonUtil.toJsonString(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
