package com.github.yedp.ez.common.model.req;

import com.github.yedp.ez.common.enums.QyWxMsgTypeEnum;

public class QyWxFileReq extends QyWxBaseReq {
    public QyWxFileReq(){
        super(QyWxMsgTypeEnum.file.name());
    }
    public QyWxFileReq(String fileId){
        this();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setMedia_id(fileId);
        this.setFile(fileInfo);
    }

    /**
     * 文件信息
     */
    private FileInfo file;

    public FileInfo getFile() {
        return file;
    }

    public void setFile(FileInfo file) {
        this.file = file;
    }

    static class FileInfo {
        /**
         * 文件id，通过QyWxUtil.uploadFile获取
         */
        private String media_id;

        /**
         * 获取文件id
         *
         * @return 文件id
         */
        public String getMedia_id() {
            return media_id;
        }

        /**
         * 设置文件id
         *
         * @param media_id 文件id
         */
        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }
    }
}
