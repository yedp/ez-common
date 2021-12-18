package com.github.yedp.ez.common.codec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.yedp.ez.common.model.resp.QyWxGroupMsg;
import com.github.yedp.ez.common.util.JsonUtil;
import com.github.yedp.ez.common.util.PoiUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoiUtilTest {
    private final static Logger log = LoggerFactory.getLogger(PoiUtilTest.class);

    @Test
    public void exportListTest(){
        try {

            String json = "[{\"id\":1,\"msgName\":\"名称1\",\"cron\":\"0 0 10 * * ?\",\"sendTime\":\"2021-12-09 10:00:00\",\"groupKey\":\"0cfe5b53-9676-4538-b685-8861bdfe9ab4\",\"qrySql\":\"sql1\",\"msgTemplate\":\"template1\",\"createTime\":\"2021-12-03 17:39:48\",\"modifyTime\":\"2021-12-08 10:10:02\",\"isDelete\":0},{\"id\":2,\"msgName\":\"名称2\",\"cron\":\"0 0 12,18,22 * * ?\",\"sendTime\":\"2021-12-08 18:00:00\",\"groupKey\":\"0cfe5b53-9676-4538-b685-8861bdfe9ab4\",\"qrySql\":\"sql2\",\"msgTemplate\":\"template2\",\"createTime\":\"2021-12-06 16:22:24\",\"modifyTime\":\"2021-12-08 14:17:44\",\"isDelete\":0},{\"id\":3,\"msgName\":\"名称3\",\"cron\":\"0 0 12,18,22 * * ?\",\"sendTime\":\"2021-12-08 18:00:00\",\"groupKey\":\"0cfe5b53-9676-4538-b685-8861bdfe9ab4\",\"qrySql\":\"sql3\",\"msgTemplate\":\"template3\",\"createTime\":\"2021-12-06 16:22:24\",\"modifyTime\":\"2021-12-08 12:00:01\",\"isDelete\":0}]";
            List<QyWxGroupMsg> list = JsonUtil.readValue(json,new TypeReference<List<QyWxGroupMsg>>(){});
            Map<String, Object> maps = new HashMap<>();
            maps.put("id","编号");
            maps.put("msgName","消息名称");
            maps.put("cron","时间表达式");
            maps.put("_TITLE_","测试标题");
            Workbook wb = PoiUtil.excelExport(maps, list, PoiUtil.FILE_EXTENSION_XLS);
            FileOutputStream fileOut = new FileOutputStream("D:/workbookout.xls");
            wb.write(fileOut);
            fileOut.close();
            wb.close();
        }catch (Exception e){
            log.error("exportListTest:{}",e);
        }
    }
}
