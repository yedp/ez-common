package com.github.yedp.ez.common.codec.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 企业微信消息 
 * </p>
 *
 * @author auto
 * @since 2021-12-03
 */
public class QyWxGroupMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 消息名称
     */
    private String msgName;

    /**
     * cron时间表达式
     */
    private String cron;

    /**
     * 发送时间
     */
    private Date sendTime;


    /**
     * 群key
     */
    private String groupKey;

    /**
     * 查询sql
     */
    private String qrySql;

    /**
     * 消息模板
     */
    private String msgTemplate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 删除标志：0-未删除 1-已删除
     */
    private Integer isDelete;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }
    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }
    public String getQrySql() {
        return qrySql;
    }

    public void setQrySql(String qrySql) {
        this.qrySql = qrySql;
    }

    public String getMsgTemplate() {
        return msgTemplate;
    }

    public void setMsgTemplate(String msgTemplate) {
        this.msgTemplate = msgTemplate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

}
