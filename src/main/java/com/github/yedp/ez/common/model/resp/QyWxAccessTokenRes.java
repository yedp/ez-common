package com.github.yedp.ez.common.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yedp
 * @date 2022/2/2111:19
 * @comment
 **/
public class QyWxAccessTokenRes extends QyWxBaseResp {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }
}
