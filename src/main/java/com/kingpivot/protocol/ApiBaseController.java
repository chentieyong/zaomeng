package com.kingpivot.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2015/9/18.
 */
public class ApiBaseController {
    private static Logger logger   = LoggerFactory.getLogger(ApiBaseController.class);

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public MessagePacket backException(HttpServletRequest request, Exception ex){
        ex.printStackTrace();
        logger.error(String.format("访问接口路径：%s,错误详情：%s",request.getRequestURI(),ex.getMessage()));
        return MessagePacket.newFail(MessageHeader.Code.serverException,ex.getMessage());
    }

}
