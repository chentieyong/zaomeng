package com.kingpivot.api.controller.ApiPageNodeController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.pageNode.PageNodeByViewUrlDto;
import com.kingpivot.base.pageNode.model.PageNode;
import com.kingpivot.base.pageNode.service.PageNodeService;
import com.kingpivot.common.utils.BeanMapper;
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
@Api(description = "页面节点管理接口")
public class ApiPageNodeController extends ApiBaseController {

    @Autowired
    private PageNodeService pageNodeService;

    @ApiOperation(value = "根据页面url获取页面节点", notes = "根据页面url获取页面节点")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "viewUrl", value = "页面url", dataType = "String")})
    @RequestMapping(value = "/getPageNodeByViewUrl")
    public MessagePacket getPageNodeByViewUrl(HttpServletRequest request) {
        String viewUrl = request.getParameter("viewUrl");

        if (StringUtils.isEmpty(viewUrl)) {
            return MessagePacket.newFail(MessageHeader.Code.viewUrlIsNull, "页面url不能为空");
        }

        PageNode pageNode = pageNodeService.getPageNodeByViewUrl(viewUrl);
        if (pageNode == null) {
            return MessagePacket.newFail(MessageHeader.Code.goodsShopIdIsError, "goodsShopID不正确");
        }

        PageNodeByViewUrlDto pageNodeByViewUrlDto = BeanMapper.map(pageNode, PageNodeByViewUrlDto.class);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", pageNodeByViewUrlDto);

        return MessagePacket.newSuccess(rsMap, "getPageNodeByViewUrl success!");
    }
}
