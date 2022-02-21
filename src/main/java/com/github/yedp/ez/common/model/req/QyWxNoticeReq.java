package com.github.yedp.ez.common.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author yedp
 * @date 2022/2/2110:52
 * @comment
 **/
public class QyWxNoticeReq implements Serializable {

    @JsonProperty("touser")
    private String touser;
    @JsonProperty("toparty")
    private String toparty;
    @JsonProperty("totag")
    private String totag;
    @JsonProperty("msgtype")
    private String msgtype;
    @JsonProperty("agentid")
    private Integer agentid;
    @JsonProperty("text")
    private TextDTO text;
    @JsonProperty("safe")
    private Integer safe;
    @JsonProperty("enable_id_trans")
    private Integer enableIdTrans;
    @JsonProperty("enable_duplicate_check")
    private Integer enableDuplicateCheck;
    @JsonProperty("duplicate_check_interval")
    private Integer duplicateCheckInterval;


    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public Integer getAgentid() {
        return agentid;
    }

    public void setAgentid(Integer agentid) {
        this.agentid = agentid;
    }

    public TextDTO getText() {
        return text;
    }

    public void setText(TextDTO text) {
        this.text = text;
    }

    public Integer getSafe() {
        return safe;
    }

    public void setSafe(Integer safe) {
        this.safe = safe;
    }

    public Integer getEnableIdTrans() {
        return enableIdTrans;
    }

    public void setEnableIdTrans(Integer enableIdTrans) {
        this.enableIdTrans = enableIdTrans;
    }

    public Integer getEnableDuplicateCheck() {
        return enableDuplicateCheck;
    }

    public void setEnableDuplicateCheck(Integer enableDuplicateCheck) {
        this.enableDuplicateCheck = enableDuplicateCheck;
    }

    public Integer getDuplicateCheckInterval() {
        return duplicateCheckInterval;
    }

    public void setDuplicateCheckInterval(Integer duplicateCheckInterval) {
        this.duplicateCheckInterval = duplicateCheckInterval;
    }

    public static class TextDTO implements Serializable {
        @JsonProperty("content")
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
