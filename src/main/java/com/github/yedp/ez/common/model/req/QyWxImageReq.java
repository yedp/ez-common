package com.github.yedp.ez.common.model.req;

import com.github.yedp.ez.common.enums.QyWxMsgTypeEnum;

import java.util.List;

public class QyWxImageReq extends QyWxBaseReq {
    public QyWxImageReq() {
        super(QyWxMsgTypeEnum.image.name());
    }

    public QyWxImageReq(String base64, String md5) {
        this();
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setBase64(base64);
        imageInfo.setMD5(md5);
        this.setImage(imageInfo);
    }


    /**
     * 文件信息
     */
    private ImageInfo image;

    public ImageInfo getImage() {
        return image;
    }

    public void setImage(ImageInfo image) {
        this.image = image;
    }

    static class ImageInfo {
        /**
         * 图片内容的base64编码
         */
        private String base64;

        /**
         * 图片内容（base64编码前）的md5值
         */
        private String MD5;

        public String getBase64() {
            return base64;
        }

        public void setBase64(String base64) {
            this.base64 = base64;
        }

        public String getMD5() {
            return MD5;
        }

        public void setMD5(String MD5) {
            this.MD5 = MD5;
        }
    }
}
