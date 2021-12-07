package com.github.yedp.ez.common.model.req;

import com.github.yedp.ez.common.enums.QyWxMsgTypeEnum;

import java.util.List;

public class QyWxNewsReq extends QyWxBaseReq {
    public QyWxNewsReq() {
        super(QyWxMsgTypeEnum.news.name());
    }

    public QyWxNewsReq(NewsInfo news) {
        this();
        this.setNews(news);
    }


    /**
     * 文件信息
     */
    private NewsInfo news;

    public NewsInfo getNews() {
        return news;
    }

    public void setNews(NewsInfo news) {
        this.news = news;
    }

    public static class NewsInfo {
        /**
         * 消息
         */
        private List<ArticlesInfo> articles;

        public List<ArticlesInfo> getArticles() {
            return articles;
        }

        public void setArticles(List<ArticlesInfo> articles) {
            this.articles = articles;
        }
    }

    public static class ArticlesInfo {
        /**
         * 标题
         */
        private String title;
        /**
         * 描述
         */
        private String description;
        /**
         * 链接
         */
        private String url;
        /**
         * 图片链接
         */
        private String picurl;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }
    }
}
