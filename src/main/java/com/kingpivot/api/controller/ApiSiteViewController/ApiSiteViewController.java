package com.kingpivot.api.controller.ApiSiteViewController;

import com.google.common.collect.Maps;
import com.kingpivot.base.siteView.model.SiteView;
import com.kingpivot.base.siteView.service.SiteViewService;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "页面访问管理接口")
public class ApiSiteViewController extends ApiBaseController {

    @Autowired
    private SiteViewService siteViewService;

    @ApiOperation(value = "提交一个页面访问", notes = "提交一个页面访问")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "ip", value = "IP地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "url", value = "来路url", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "operatingSystem", value = "操作系统", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "browserType", value = "浏览器", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "int"),
    })
    @RequestMapping(value = "/submitOneSiteView")
    public MessagePacket submitOneSiteView(HttpServletRequest request) {
        String applicationID = request.getParameter("applicationID");
        String ip = request.getParameter("ip");
        String url = request.getParameter("url");
        String operatingSystem = request.getParameter("operatingSystem");
        String browserType = request.getParameter("browserType");
        String address = request.getParameter("address");

        SiteView siteView = new SiteView();
        siteView.setApplicationID(applicationID);
        siteView.setIp(ip);
        siteView.setUrl(url);
        siteView.setOperatingSystem(operatingSystem);
        siteView.setBrowserType(browserType);
        siteView.setAddress(address);
        siteViewService.save(siteView);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", siteView.getId());
        return MessagePacket.newSuccess(rsMap, "submitOneSiteView success!");
    }
}
