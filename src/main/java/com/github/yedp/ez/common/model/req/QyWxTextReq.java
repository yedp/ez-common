package com.github.yedp.ez.common.model.req;

import com.github.yedp.ez.common.enums.QyWxMsgTypeEnum;

import java.util.List;

public class QyWxTextReq extends QyWxBaseReq {
    public QyWxTextReq() {
        super(QyWxMsgTypeEnum.text.name());
    }

    public QyWxTextReq(String content) {
        this();
        TextInfo textInfo = new TextInfo();
        textInfo.setContent(content);
        this.setText(textInfo);
    }

    public QyWxTextReq(String content, List<String> mentionedMobileList) {
        this(content);
        text.setMentioned_mobile_list(mentionedMobileList);
    }

    /**
     * 文件信息
     */
    private TextInfo text;

    public TextInfo getText() {
        return text;
    }

    public void setText(TextInfo text) {
        this.text = text;
    }

    static class TextInfo {
        /**
         * 消息
         */
        private String content;

        /**
         * 需要关注消息手机号码人员
         */
        private List<String> mentioned_mobile_list;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getMentioned_mobile_list() {
            return mentioned_mobile_list;
        }

        public void setMentioned_mobile_list(List<String> mentioned_mobile_list) {
            this.mentioned_mobile_list = mentioned_mobile_list;
        }
    }
}
