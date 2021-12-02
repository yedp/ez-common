package com.github.yedp.ez.common.util;

import com.github.yedp.ez.common.enums.RetCodeEnum;
import com.github.yedp.ez.common.model.RetObj;
import com.github.yedp.ez.common.model.req.QyWxFileReq;
import com.github.yedp.ez.common.model.req.QyWxMarkdownReq;
import com.github.yedp.ez.common.model.req.QyWxTextReq;
import com.github.yedp.ez.common.model.resp.QyWxBaseResp;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class QyWxUtil {
    private static final String UPLOAD_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/upload_media?type=file&key=";
    private static final String SEND_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=";


    /**
     * 上传文件到企业微信
     *
     * @param groupId 群key
     * @param file    文件
     * @return 文件id
     * @throws IOException io异常
     */
    public static RetObj uploadFile(String groupId, File file) throws IOException {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addBinaryBody("file", file);
        String res = HttpUtil.ReturnPostBody(UPLOAD_URL.concat(groupId), multipartEntityBuilder.build());
        if (StringUtils.isEmpty(res)) {
            return RetObj.error("上传失败");
        }
        HashMap map = JsonUtil.readValue(res, HashMap.class);
        if (map != null) {
            Object o = map.get("media_id");
            if (o != null) {
                return RetObj.success(o.toString());
            }
        }
        return RetObj.error(RetCodeEnum.BUSINESS_ERROR, map);
    }

    /**
     * 发送文件到群
     *
     * @param groupId 群key
     * @param fileId  文件id
     * @return 结果
     * @throws IOException 异常
     */
    public static RetObj sendFile(String groupId, String fileId) throws IOException {
        QyWxFileReq req = new QyWxFileReq(fileId);
        String res = HttpUtil.ReturnPostBody(SEND_URL.concat(groupId), JsonUtil.toJsonString(req));
        return parseRetObj(res);
    }


    /**
     * 发送消息
     *
     * @param groupId 群key
     * @param msg     消息
     * @return 结果
     * @throws IOException 异常
     */
    public static RetObj sendMsg(String groupId, String msg) throws IOException {
        QyWxTextReq req = new QyWxTextReq(msg);
        String res = HttpUtil.ReturnPostBody(SEND_URL.concat(groupId), JsonUtil.toJsonString(req));
        return parseRetObj(res);
    }

    /**
     * 发送消息
     *
     * @param groupId            群key
     * @param msg                消息
     * @param mentionedPhoneList 需关注人的手机号码
     * @return 结果
     * @throws IOException 异常
     */
    public static RetObj sendMsg(String groupId, String msg, List<String> mentionedPhoneList) throws IOException {
        QyWxTextReq req = new QyWxTextReq(msg, mentionedPhoneList);
        String res = HttpUtil.ReturnPostBody(SEND_URL.concat(groupId), JsonUtil.toJsonString(req));
        return parseRetObj(res);
    }

    /**
     * 发送消息markdown格式
     *
     * @param groupId 群key
     * @param msg     消息
     * @return 结果
     * @throws IOException 异常
     */
    public static RetObj sendMsgMarkdown(String groupId, String msg) throws IOException {
        QyWxMarkdownReq req = new  QyWxMarkdownReq(msg);
        String res = HttpUtil.ReturnPostBody(SEND_URL.concat(groupId), JsonUtil.toJsonString(req));
        return parseRetObj(res);
    }

    /**
     * 解析返回字符串为返回对象
     *
     * @param res 返回字符串
     * @return 返回对象
     */
    private static RetObj parseRetObj(String res) throws IOException {
        if (StringUtils.isEmpty(res)) {
            return RetObj.error(res);
        }
        QyWxBaseResp resp = JsonUtil.readValue(res, QyWxBaseResp.class);
        if (resp != null) {
            if (resp.getErrcode() != null && resp.getErrcode() == 0) {
                return RetObj.success(resp);
            }
        }
        return RetObj.error(RetCodeEnum.BUSINESS_ERROR, resp);
    }

}
