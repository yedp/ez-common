package com.github.yedp.ez.common.codec;

import com.github.yedp.ez.common.model.RetObj;
import com.github.yedp.ez.common.util.JsonUtil;
import com.github.yedp.ez.common.util.QyWxUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QyWxUtilTest {
    private String groupId = "0cfe5b53-9676-4538-b685-8861bdfe9ab4";

    @Test
    public void testUpload() throws IOException {
        RetObj retObj = QyWxUtil.uploadFile(groupId, new File("d:/test.xlsx"));
        System.out.println(JsonUtil.toJsonString(retObj));
    }


    @Test
    public void tesSendFile() throws IOException {
        RetObj retObj = QyWxUtil.sendFile(groupId, "3yYCulFcqkfUt6HuD0_VL3rzh9Y5cvshlMLFGGrSL0V7pCqFz8Z3Y4ogpulsMQSfy");
        System.out.println(JsonUtil.toJsonString(retObj));
    }

    @Test
    public void tesSendMsg() throws IOException {
        RetObj retObj = QyWxUtil.sendMsg(groupId, "测试消息");
        System.out.println(JsonUtil.toJsonString(retObj));
    }

    @Test
    public void tesSendMsgWithMentioned() throws IOException {
        List<String> mentionedList = new ArrayList<>();
        mentionedList.add("@all");
        RetObj retObj = QyWxUtil.sendMsg(groupId, "测试消息", mentionedList);
        System.out.println(JsonUtil.toJsonString(retObj));
    }

    @Test
    public void tesSendMsgMarkdown() throws IOException {
        RetObj retObj = QyWxUtil.sendMsgMarkdown(groupId, "测试**加粗**");
        System.out.println(JsonUtil.toJsonString(retObj));
    }
}
