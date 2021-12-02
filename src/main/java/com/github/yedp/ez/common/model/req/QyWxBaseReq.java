package com.github.yedp.ez.common.model.req;

public class QyWxBaseReq {
    public QyWxBaseReq(){}
    public QyWxBaseReq(String msgType){
        this.setMsgtype(msgType);
    }
    /**
     *类型: file、text、markdown
     */
    private String msgtype;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
}
