package com.kingpivot.common.weiXinViolationCheck;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class CheckTask extends RecursiveTask<AccessTokenWX> {

    private String url;
    private MultipartFile file;
    private String content;

    public CheckTask() {
    }

    public CheckTask(String url, MultipartFile file, String content) {
        this.url = url;
        this.file = file;
        this.content = content;
    }

    @Override
    protected AccessTokenWX compute() {
        AccessTokenWX result = new AccessTokenWX();
        try {
            if (null == content) {
                String json = UploadAction.uploadFile(url, file);
                result = JSON.parseObject(json, AccessTokenWX.class);
            } else {
                RestTemplate rest = new RestTemplate();
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("content", content);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity requestEntity = new HttpEntity(param, headers);
                ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
                String json = new String(entity.getBody(), "utf-8");
                result = JSON.parseObject(json, AccessTokenWX.class);
            }
        } catch (Exception e) {
            result.setErrCode("500");
            result.setErrMsg("system错误");
        }
        return result;
    }
}
