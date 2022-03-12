package com.github.yedp.ez.common.codec;

import com.github.yedp.ez.common.model.RetObj;
import com.github.yedp.ez.common.model.resp.QyWxAccessTokenRes;
import com.github.yedp.ez.common.util.FileUtil;
import com.github.yedp.ez.common.util.JsonUtil;
import com.github.yedp.ez.common.util.QyWxUtil;
import netscape.javascript.JSObject;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
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

    @Test
    public void tesSendImage() throws IOException {
        File file = new File("D:/qrcode.png");
        RetObj retObj = QyWxUtil.sendImage(groupId, file);
        System.out.println(JsonUtil.toJsonString(retObj));
    }

    @Test
    public void tesSendNews() throws IOException {
        RetObj retObj = QyWxUtil.sendNews(groupId, "测试标题", "测试描述", "www.qq.com", "http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png");
        System.out.println(JsonUtil.toJsonString(retObj));
    }

    @Test
    public void getAccessToken() throws IOException {
        String corpId = "ww957f9297596400e8";
        String secret  = "1WhPNCOtdaXxA308S2ooN3pEvZwkcPt6srH5YK4RGtQ";
        String accessToken = null;
        RetObj<QyWxAccessTokenRes> resRetObj= QyWxUtil.getAccessToken(corpId,secret);
        if(resRetObj.getCode() == 200 && resRetObj.getData() != null){
            accessToken =   resRetObj.getData().getAccessToken();
        }
        System.out.println(accessToken);
    }

    @Test
    public void sendNotice() throws IOException {
        Integer agentId = 1000002;
        String accessToken = "ezOp8xTWSZy2sOldF44boGwr8hohRfzhKP4x5oqF_aIJWv-VhCbJR9-24lY4iss7Qcb1V2XQtQ0RAGfJg4NSk-BU-kyT7fBimrfmbtCcjFT9TdqcJLCVx6ojfawbu0SVW6TDz32zss9CnHzlGMLCdnwnkcGUF6WskFiLpMZuEjD9vBHyayzfjATt-Co-2dbKhRpPdaIXwqETUlkFfIZlxA";
        QyWxUtil.sendNotification(accessToken,agentId,"YeDaoPing","测试消息");
    }

}
