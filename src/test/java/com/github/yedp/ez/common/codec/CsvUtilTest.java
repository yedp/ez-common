package com.github.yedp.ez.common.codec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.yedp.ez.common.codec.vo.QyWxGroupMsg;
import com.github.yedp.ez.common.util.CsvUtils;
import com.github.yedp.ez.common.util.JsonUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CsvUtilTest {
    private final static Logger log = LoggerFactory.getLogger(CsvUtilTest.class);

    static String filePath = "D:/qy_wx_group_msg.csv";


    @Test
    public void testRead() {
        try {
            List<QyWxGroupMsg> list = CsvUtils.read(new File(filePath), QyWxGroupMsg.class);
            log.info("list: {}", JsonUtil.toJsonString(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void export() throws IOException {
        String json = "[{\"id\":1,\"msgName\":\"名称1\",\"cron\":\"0 0 10 * * ?\",\"sendTime\":\"2021-12-09 10:00:00\",\"groupKey\":\"0cfe5b53-9676-4538-b685-8861bdfe9ab4\",\"qrySql\":\"sql1\",\"msgTemplate\":\"template1\",\"createTime\":\"2021-12-03 17:39:48\",\"modifyTime\":\"2021-12-08 10:10:02\",\"isDelete\":0},{\"id\":2,\"msgName\":\"名称2\",\"cron\":\"0 0 12,18,22 * * ?\",\"sendTime\":\"2021-12-08 18:00:00\",\"groupKey\":\"0cfe5b53-9676-4538-b685-8861bdfe9ab4\",\"qrySql\":\"sql2\",\"msgTemplate\":\"template2\",\"createTime\":\"2021-12-06 16:22:24\",\"modifyTime\":\"2021-12-08 14:17:44\",\"isDelete\":0},{\"id\":3,\"msgName\":\"名称3\",\"cron\":\"0 0 12,18,22 * * ?\",\"sendTime\":\"2021-12-08 18:00:00\",\"groupKey\":\"0cfe5b53-9676-4538-b685-8861bdfe9ab4\",\"qrySql\":\"sql3\",\"msgTemplate\":\"template3\",\"createTime\":\"2021-12-06 16:22:24\",\"modifyTime\":\"2021-12-08 12:00:01\",\"isDelete\":0}]";
        List<QyWxGroupMsg> list = JsonUtil.readValue(json,new TypeReference<List<QyWxGroupMsg>>(){});
        FileOutputStream fio = new FileOutputStream(filePath);
        CsvUtils.export(QyWxGroupMsg.class,list,fio);
    }

}
