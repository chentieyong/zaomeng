package com.kingpivot.api.controller.ApiAddressController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.weixin.HttpUtil;
import com.kingpivot.base.config.Config;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

@RequestMapping("/api")
@RestController
@Api(description = "地址管理接口")
public class ApiAddressController extends ApiBaseController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "坐标获取地址", notes = "坐标获取地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mapX", value = "经度", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "mapY", value = "纬度", dataType = "String")})
    @RequestMapping(value = "/getAddressByMap")
    public MessagePacket getAddressByMap(HttpServletRequest request) {
        String mapX = request.getParameter("mapX");
        String mapY = request.getParameter("mapY");
        if (StringUtils.isEmpty(mapX) || StringUtils.isEmpty(mapY)) {
            return MessagePacket.newFail(MessageHeader.Code.loginTypeIsNull, "坐标异常");
        }
        Map<String, String> param = new HashMap<>();
        param.put("location", String.format("%s,%s", mapY, mapX));
        param.put("key", Config.TENXUNMAP_KEY);
        String info = null;
        try {
            info = HttpUtil.doGet(Config.TENXUNMAP_URL, param);
            logger.info("address=[{}]", info);
            JSONObject jsonObject = JSONObject.fromObject(info);
            Map result = (Map) jsonObject.get("result");
            String address = (String) result.get("address");
            Map<String, Object> rsMap = Maps.newConcurrentMap();
            rsMap.put("data", address);
            return MessagePacket.newSuccess(rsMap, "getAddressByMap success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MessagePacket.newFail(MessageHeader.Code.loginTypeIsNull, "坐标异常");
    }

}
