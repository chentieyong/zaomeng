package com.kingpivot.common.weixinPay;

import com.kingpivot.common.utils.XStreamUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;


public class WechatPayUtils {
    private static String CHARSET = "UTF-8";
    //统一订单url
    final static String GEN_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 随机16为数值
     *
     * @return
     */
    public static String buildRandom() {
        String currTime = TenpayUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < 4; i++) {
            num = num * 10;
        }
        return (int) ((random * num)) + strTime;
    }

    /**
     * 获取ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("PRoxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (null == ip) {
            ip = "";
        }
        if (StringUtils.isNotEmpty(ip)) {
            String[] ipArr = ip.split(",");
            if (ipArr.length > 1) {
                ip = ipArr[0];
            }
        }
        return ip;
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 sign
     *
     * @param partnerKey 商户密钥
     */
    public static String createSign(Map<String, Object> map, String partnerKey) {
        map.remove("sign");
        SortedMap<String, String> packageParams = new TreeMap<>();
        for (Map.Entry<String, Object> m : map.entrySet()) {
            if (null != m.getValue()) {
                packageParams.put(m.getKey(), m.getValue().toString());
            }
        }

        StringBuffer sb = new StringBuffer();
        Set<?> es = packageParams.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!StringUtils.isEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + partnerKey);
        String sign = MD5Util.MD5Encode(sb.toString(), CHARSET).toUpperCase();
        return sign;
    }

    public static String getOrderNo(String partner) {
        String order = partner
                + new SimpleDateFormat("yyyyMMdd").format(new Date());
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            order += r.nextInt(9);
        }
        return order;
    }

    public static PayOrderResponseInfo doGenOrderNo(WechatPayInfo payInfo) throws Exception {
        String result = doGenOrder(XStreamUtil.getXstream(WechatPayInfo.class).toXML(payInfo));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>.payInfo:"+result);
        return (PayOrderResponseInfo) XStreamUtil.getXstream(PayOrderResponseInfo.class).fromXML(result);
    }

    /**
     * 水务发送短信
     *
     * @param data 发送报文
     */
    public static String doGenSms(String data,String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                String jsonStr = toStringInfo(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    /**
     * 发送xml请求到server端
     * @param url xml请求数据地址
     * @param xmlString 发送的xml数据流
     * @return null发送失败，否则返回响应内容
     */
    public static String sendPost(String url,String xmlString){
        //创建httpclient工具对象
        HttpClient client = new HttpClient();
        //创建post请求方法
        PostMethod myPost = new PostMethod(url);
        //设置请求超时时间
        client.setConnectionTimeout(3000*1000);
        String responseString = null;
        try{
            //设置请求头部类型
            myPost.setRequestHeader("Content-Type","text/xml");
            myPost.setRequestHeader("charset","utf-8");
            //设置请求体，即xml文本内容，一种是直接获取xml内容字符串，一种是读取xml文件以流的形式
            myPost.setRequestBody(xmlString);
            int statusCode = client.executeMethod(myPost);
            //只有请求成功200了，才做处理
            if(statusCode == HttpStatus.SC_OK){
                InputStream inputStream = myPost.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                responseString = stringBuffer.toString();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            myPost.releaseConnection();
        }
        return responseString;
    }

    /**
     * 发送红包
     *
     * @param data 发送报文
     */
    public static String doGenOrder(String data) throws Exception {
        CloseableHttpClient httpclient = HttpClients.custom().build();
        try {
            HttpPost httpost = new HttpPost(GEN_ORDER_URL); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                String jsonStr = toStringInfo(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    private static String toStringInfo(HttpEntity entity, String defaultCharset) throws Exception, IOException {
        final InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        try {
            Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
                    "HTTP entity too large to be buffered in memory");
            int i = (int) entity.getContentLength();
            if (i < 0) {
                i = 4096;
            }
            Charset charset = null;

            if (charset == null) {
                charset = Charset.forName(defaultCharset);
            }
            if (charset == null) {
                charset = HTTP.DEF_CONTENT_CHARSET;
            }
            final Reader reader = new InputStreamReader(instream, charset);
            final CharArrayBuffer buffer = new CharArrayBuffer(i);
            final char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            return buffer.toString();
        } finally {
            instream.close();
        }
    }
}
