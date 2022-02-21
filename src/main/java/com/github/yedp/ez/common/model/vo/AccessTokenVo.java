package com.github.yedp.ez.common.model.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yedp
 * @date 2022/2/2111:39
 * @comment
 **/
public class AccessTokenVo implements Serializable {
    private String accessToken;
    private Date expireTime;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
