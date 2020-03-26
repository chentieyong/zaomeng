package com.kingpivot.api.dto.weixin;

import com.kingpivot.common.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public abstract class HttpUtil {

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final String JIXINTONG_CHARSET = "GB2312";

    public static String doGet(String url) throws IOException {
        return doGet(url, null);
    }

    public static String doGet(String url, Map<String, String> params) throws IOException {
        String result = null;
        InputStream instream = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(30000).build();//设置请求和传输超时时间
            StringBuilder urlGet = new StringBuilder(url);
            String urlParam = buildQuery(params, DEFAULT_CHARSET);
            if (StringUtils.isNotEmpty(urlParam)) {
                if (url.contains("?") && url.contains("=")) {
                    urlGet.append("&");
                } else {
                    urlGet.append("?");
                }
                urlGet.append(urlParam);
            }
            HttpGet httpget = new HttpGet(urlGet.toString());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                instream = entity.getContent();
                result = getStreamAsString(instream, DEFAULT_CHARSET);
            }
        } finally {
            if (null != instream)
                instream.close();
        }
        return result;
    }

    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder query = new StringBuilder();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;

        for (Map.Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (StringUtil.areNotEmpty(name, value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }

                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }

        return query.toString();
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        String result = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(stream, charset));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();

        } finally {
            if (null != stream) {
                stream.close();
            }
            if (null != in) {
                in.close();
            }
        }
        return result;
    }

    /**
     * 发送HttpPost请求
     *
     * @param url_req 服务地址
     * @param params
     * @return 成功:返回流<br/>
     */
    public static InputStream postWx(String url_req, String params) {
        try {
            URL url = new URL(url_req);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();
            // 读取响应
            InputStream is = connection.getInputStream();
            return is;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; // 自定义错误信息
    }
}
