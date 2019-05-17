package com.kingpivot.api.controller.ApiParameterController;

import com.google.common.collect.Maps;
import com.kingpivot.base.parameter.service.ParameterService;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "系统参数")
public class ApiParameterController extends ApiBaseController {

    @Autowired
    private ParameterService parameterService;

    @ApiOperation(value = "根据code获取系统参数值", notes = "根据code获取系统参数值")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "code", value = "code", dataType = "String")})
    @RequestMapping(value = "/getParameterValueByCode")
    public MessagePacket getParameterValueByCode(HttpServletRequest request) {
        String code = request.getParameter("code");

        if (StringUtils.isEmpty(code)) {
            return MessagePacket.newFail(MessageHeader.Code.codeIsNull, "code不能为空");
        }

        String val = parameterService.getParemeterValueByCode(code);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", val);

        return MessagePacket.newSuccess(rsMap, "getParameterValueByCode success!");
    }
}
