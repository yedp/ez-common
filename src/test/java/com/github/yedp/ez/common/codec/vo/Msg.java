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
public class Msg implements Serializable {
    public Msg(Integer id, String msgName, String cron) {
        this.id = id;
        this.msgName = msgName;
        this.cron = cron;
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
