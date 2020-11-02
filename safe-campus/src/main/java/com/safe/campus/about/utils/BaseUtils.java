package com.safe.campus.about.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author HT
 */
public class BaseUtils {

    private BaseUtils () {

    }

    /**
     * 生成随机ID
     * @return
     */
    public static String getRandomId () {
        return UUID.randomUUID().toString();
    }

    /**
     * 初始化CloseableHttpClient
     * @return
     */
    private static CloseableHttpClient initHttpClient () {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(20);
        cm.setDefaultMaxPerRoute(50);
        return HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * get请求
     * @param url
     *          请求URL
     * @return
     */
    public static String httpGet(String url) {
        CloseableHttpClient httpClient = initHttpClient();
        CloseableHttpResponse response = null;
        BufferedReader in = null;
        String result = "";
        try {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpGet.setConfig(requestConfig);
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("Content-type", "application/json; charset=utf-8");
            httpGet.setHeader("Accept", "application/json");
            response = httpClient.execute(httpGet);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * post请求
     * @param url
     *          请求URL
     * @param param
     *          参数
     * @return
     */
    public static String httpPost(String url, String param) {
        CloseableHttpClient httpClient = initHttpClient();
        CloseableHttpResponse response = null;
        BufferedReader in = null;
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpPost.setConfig(requestConfig);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-type","application/json;charset=utf-8");
            httpPost.addHeader("Accept", "application/json");
            StringEntity entity = new StringEntity(param, Charset.forName("UTF-8"));
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将传入的字符串转成Unicode编码方式
     * @param s
     * @return
     */
    public static String toUnicode (String s) {
        String result = "";
        if (!StringUtils.isEmpty(s)) {
            char[] chars = s.toCharArray();
            for (char c : chars) {
                // 汉字范围 \u4e00-\u9fa5 (中文)}
                if (c >= 19968 && c <= 171941) {
                    String hex = Integer.toString(c, 16);
                    int loopTimes = 4 - hex.length();
                    for (int i = 0; i < loopTimes; i++) {
                        hex = "0" + hex;
                    }
                    result += "\\u" + hex;
                } else {
                    result += c;
                }
            }
        }
        return result;
    }

    /**
     * 取到传入日期的零点零分零秒
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0);
        return calendar.getTime();
    }

    /**
     * 获取传入日期的最后一个时刻
     * @param date
     * @return
     */
    public static Date getEndOfDay (Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                23,
                59,
                59);
        return calendar.getTime();
    }

}
