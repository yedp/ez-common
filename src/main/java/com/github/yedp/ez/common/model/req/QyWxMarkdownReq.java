package com.github.yedp.ez.common.model.req;

import com.github.yedp.ez.common.enums.QyWxMsgTypeEnum;

import java.util.List;

public class QyWxMarkdownReq extends QyWxBaseReq {
    public QyWxMarkdownReq() {
        super(QyWxMsgTypeEnum.markdown.name());
    }

    public QyWxMarkdownReq(String content) {
        this();
        MarkdownInfo markdownInfo = new MarkdownInfo();
        markdownInfo.setContent(content);
        this.setMarkdown(markdownInfo);
    }


    /**
     * 文件信息
     */
    private MarkdownInfo markdown;

    public MarkdownInfo getMarkdown() {
        return markdown;
    }

    public void setMarkdown(MarkdownInfo markdown) {
        this.markdown = markdown;
    }

    static class MarkdownInfo {
        /**
         * 消息
         */
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}

