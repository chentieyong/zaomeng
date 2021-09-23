package com.kingpivot.common.util;

import com.kingpivot.common.weiXinViolationCheck.AccessTokenWX;
import com.kingpivot.common.weiXinViolationCheck.CheckTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class WeiXinViolationCheckUtil {

    /**
     * 微信图片检测接口
     */
    static String IMG_CHECK = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=";
    /**
     * 微信文字检测接口
     */
    static String TXT_CHECK = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=";

    static private Logger logger = LoggerFactory.getLogger(WeiXinViolationCheckUtil.class);

    /**
     * file:图片路径地址
     * content:文字内容
     * token:微信token
     */
    public static Map checkImgOrMsg(MultipartFile file, String content, String token) throws ExecutionException, InterruptedException {
        Map<String, Object> resultMap = new HashMap<>();
        ForkJoinPool pool = new ForkJoinPool();
        String contentUrl = TXT_CHECK + token;
        String imgUrl = IMG_CHECK + token;
        // 返回为0说明检测正常
        String ok = "0";
        ForkJoinTask<AccessTokenWX> contentResult = null;
        ForkJoinTask<AccessTokenWX> imgResult = null;
        resultMap.put("all", "ok");

        if (null != content && content.trim().length() > 0) {
            CheckTask contentTask = new CheckTask(contentUrl, null, content);
            contentResult = pool.submit(contentTask).fork();
            logger.info("文字检测结果===" + contentResult.get());
        }
        if (null != file && !file.isEmpty()) {
            CheckTask contentTask = new CheckTask(imgUrl, file, null);
            imgResult = pool.submit(contentTask).fork();
            logger.info("图片检测结果===" + imgResult.get());
        }
        if (null != contentResult) {
            resultMap.put("content", contentResult.get().getErrCode());
            if (!ok.equals(contentResult.get().getErrCode())) {
                resultMap.put("all", "err");
            }
        }
        if (null != imgResult) {
            resultMap.put("img", imgResult.get().getErrCode());
            if (!ok.equals(imgResult.get().getErrCode())) {
                resultMap.put("all", "err");
            }
        }
        return resultMap;
    }

//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        Map map = checkImgOrMsg(null, "习近平666", "");
//    }
}
