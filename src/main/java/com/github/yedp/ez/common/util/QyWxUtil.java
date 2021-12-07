package com.github.yedp.ez.common.util;

import com.github.yedp.ez.common.enums.RetCodeEnum;
import com.github.yedp.ez.common.model.RetObj;
import com.github.yedp.ez.common.model.req.*;
import com.github.yedp.ez.common.model.resp.QyWxBaseResp;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        QyWxMarkdownReq req = new QyWxMarkdownReq(msg);
        String res = HttpUtil.ReturnPostBody(SEND_URL.concat(groupId), JsonUtil.toJsonString(req));
        return parseRetObj(res);
    }

    /**
     * 发送图片
     *
     * @param groupId 群key
     * @param base64 图片内容的base64编码
     * @param md5 图片内容（base64编码前）的md5值
     * @return  结果
     * @throws IOException io异常信息
     */
    public static RetObj sendImage(String groupId, String base64, String md5) throws IOException {
        QyWxImageReq req = new QyWxImageReq(base64, md5);
        String res = HttpUtil.ReturnPostBody(SEND_URL.concat(groupId), JsonUtil.toJsonString(req));
        return parseRetObj(res);
    }

    /**
     * 发送图片
     *
     * @param groupId 群key
     * @param file    图片文件
     * @return 结果
     * @throws IOException io异常信息
     */
    public static RetObj sendImage(String groupId, File file) throws IOException {
        QyWxImageReq req = new QyWxImageReq(FileUtil.toBase64(file), FileUtil.toMd5(file));
        String res = HttpUtil.ReturnPostBody(SEND_URL.concat(groupId), JsonUtil.toJsonString(req));
        return parseRetObj(res);
    }

    /**
     * 发送图文消息
     *
     * @param groupId 群key
     * @param title 标题
     * @param description 描述
     * @param url  链接
     * @param picUrl 图片链接
     * @return 结果
     * @throws IOException io异常
     */
    public static RetObj sendNews(String groupId, String title,String description,String url ,String picUrl) throws IOException {
        QyWxNewsReq.ArticlesInfo articlesInfo = new QyWxNewsReq.ArticlesInfo();
        articlesInfo.setTitle(title);
        articlesInfo.setDescription(description);
        articlesInfo.setUrl(url);
        articlesInfo.setPicurl(picUrl);
        List<QyWxNewsReq.ArticlesInfo> articlesInfos = new ArrayList<>();
        articlesInfos.add(articlesInfo);
        return sendNews(groupId,articlesInfos);
    }

    /**
     * 发送图文消息
     *
     * @param groupId 群key
     * @param articlesInfo 图文信息
     * @return 结果
     * @throws IOException io异常
     */
    public static RetObj sendNews(String groupId, QyWxNewsReq.ArticlesInfo articlesInfo) throws IOException {
        List<QyWxNewsReq.ArticlesInfo> articlesInfos = new ArrayList<>();
        articlesInfos.add(articlesInfo);
        return sendNews(groupId,articlesInfos);
    }

    /**
     * 发送图文消息
     *
     * @param groupId 群key
     * @param articlesInfos 图文信息列表
     * @return 结果
     * @throws IOException io异常
     */
    public static RetObj sendNews(String groupId, List<QyWxNewsReq.ArticlesInfo> articlesInfos) throws IOException {
        QyWxNewsReq.NewsInfo newsInfo = new QyWxNewsReq.NewsInfo();
        newsInfo.setArticles(articlesInfos);

        QyWxNewsReq req = new QyWxNewsReq(newsInfo);
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
