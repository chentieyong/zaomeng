package com.kingpivot.api.controller.ApiPartyMemberController;

import com.google.common.collect.Maps;
import com.kingpivot.base.partyMember.model.PartyMember;
import com.kingpivot.base.partyMember.service.PartyMemberService;
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
@Api(description = "党员接口")
public class ApiPartyMemberController extends ApiBaseController {

    @Autowired
    private PartyMemberService partyMemberService;

    @ApiOperation(value = "submitOnePartyMember", notes = "提交一个党员记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "党员姓名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "joinYear", value = "加入年份", dataType = "String")
    })
    @RequestMapping(value = "/submitOnePartyMember")
    public MessagePacket submitOnePartyMember(HttpServletRequest request) {
        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "姓名不能为空");
        }
        String applicationID = request.getParameter("applicationID");//应用id
        if (StringUtils.isEmpty(applicationID)) {
            return MessagePacket.newFail(MessageHeader.Code.applicationIdIsNull, "应用id不能为空");
        }
        String joinYear = request.getParameter("joinYear");//joinYear
        if (StringUtils.isEmpty(joinYear)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "joinYear不能为空");
        }
        PartyMember partyMember = partyMemberService.findByName(name);
        if (partyMember == null) {
            partyMember = new PartyMember();
            partyMember.setApplicationID(applicationID);
            partyMember.setName(name);
            partyMember.setJoinYear(joinYear);
            partyMemberService.save(partyMember);
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", partyMember.getId());
        return MessagePacket.newSuccess(rsMap, "submitOnePartyMember success!");
    }
}
