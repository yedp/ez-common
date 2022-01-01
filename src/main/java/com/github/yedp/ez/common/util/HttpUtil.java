package com.github.yedp.ez.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class HttpUtil {
    private static CloseableHttpClient getHttpClient() {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(20000)
                .setConnectTimeout(20000)
                .setConnectionRequestTimeout(20000)
                .build();

        return HttpClientBuilder
                .create()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
    }

    private static CloseableHttpClient getHttpClient(int timeout) {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();

        return HttpClientBuilder
                .create()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
    }

    public static <T> T returnPostObj(String url, String body, Class<T> valueType) throws IOException {
        String str = returnPostBody(url, body, "application/json");
        return JsonUtil.readValue(str, valueType);
    }

    public static String returnPostBody(String url, String body) throws IOException {
        return returnPostBody(url, body, "application/json");
    }

    public static String returnPostBody(String url, String body, String contentType) throws IOException {
        StringEntity entity = null;

        if (StringUtils.isNotEmpty(body)) {
            entity = new StringEntity(body, "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType(contentType);
        }

        return returnPostBody(url, entity, "UTF-8");
    }

    public static String returnPostBody(String url, HttpEntity entity) throws IOException {
        return returnPostBody(url, entity, "UTF-8");
    }

    private static String returnPostBody(String url, HttpEntity entity, String responseCharset) throws IOException {
        return returnPostBody(url, entity, responseCharset, null);
    }

    private static String returnPostBody(String url, HttpEntity entity, String responseCharset, Map<String, String> headers) throws IOException {
        HttpPost post = new HttpPost(url);
        if (entity != null) {
            post.setEntity(entity);
        }
        if (headers != null && headers.size() > 0) {
            headers.keySet().forEach(key -> post.setHeader(key, headers.get(key)));
        }
        HttpResponse response = getHttpClient().execute(post);
        HttpEntity responseEntity = response.getEntity();
        return EntityUtils.toString(responseEntity, responseCharset);
    }

    public static BufferedImage returnPostBodyImage(String url, String body) throws IOException {
        StringEntity entity = null;
        if (StringUtils.isNotEmpty(body)) {
            entity = new StringEntity(body, "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json; charset=UTF-8");
        }

        HttpPost post = new HttpPost(url);
        if (entity != null) {
            post.setEntity(entity);
        }
        HttpResponse response = getHttpClient().execute(post);
        HttpEntity responseEntity = response.getEntity();
        byte[] data = EntityUtils.toByteArray(responseEntity);
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        return ImageIO.read(bais);

    }

    public static String returnPostBody(String url, HttpEntity entity, Map<String, String> headers) throws IOException {
        return returnPostBody(url, entity, "UTF-8", headers);
    }

    public static String returnPostBody(String url, HttpEntity entity, Map<String, String> headers, int timeout) throws IOException {
        HttpPost post = new HttpPost(url);
        if (entity != null) {
            post.setEntity(entity);
        }

        if (headers != null && headers.size() > 0) {
            headers.keySet().forEach(key -> post.setHeader(key, headers.get(key)));
        }

        HttpResponse response = getHttpClient(timeout).execute(post);
        HttpEntity responseEntity = response.getEntity();
        return EntityUtils.toString(responseEntity, "UTF-8");
    }

    public static String returnGetBody(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpresponse = getHttpClient().execute(httpGet);
        HttpEntity entity = httpresponse.getEntity();
        return EntityUtils.toString(entity);
    }

}
