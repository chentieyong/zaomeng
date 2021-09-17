package com.kingpivot.api.controller.ApiMemberWishController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.memberWish.MemberWishListDto;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.base.memberWish.model.MemberWish;
import com.kingpivot.base.memberWish.service.MemberWishService;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.utils.*;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import com.kingpivot.protocol.MessagePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "会员愿望接口")
public class ApiMemberWishController extends ApiBaseController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberWishService memberWishService;

    @ApiOperation(value = "submitOneMemberWish", notes = "提交一个会员愿望")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "memberID", value = "会员id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String")
    })
    @RequestMapping(value = "/submitOneMemberWish")
    public MessagePacket submitOneMemberWish(HttpServletRequest request) {
        String memberID = request.getParameter("memberID");
        if (StringUtils.isEmpty(memberID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberIDIsNull, "会员id不能为空");
        }
        Member member = memberService.findById(memberID);
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberIDIsNull, "会员不存在");
        }

        String name = request.getParameter("name");
        String description = request.getParameter("description");//说明

        MemberWish memberWish = new MemberWish();
        if (StringUtils.isNotBlank(name)) {
            memberWish.setName(name);
        } else {
            memberWish.setName(String.format("会员%s许愿", member.getName()));
        }
        memberWish.setMemberID(memberID);
        memberWish.setApplicationID(member.getApplicationID());
        memberWish.setDescription(description);
        memberWishService.save(memberWish);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", memberWish.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneMemberWish success!");
    }

    @ApiOperation(value = "getMemberWishList", notes = "获取会员心愿列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "memberID", value = "会员id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getMemberWishList")
    public MessagePacket getMemberWishList(HttpServletRequest request) {
        String applicationID = request.getParameter("applicationID");
        if (StringUtils.isEmpty(applicationID)) {
            return MessagePacket.newFail(MessageHeader.Code.applicationIdIsNull, "applicationID不能为空");
        }
        String memberID = request.getParameter("memberID");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("applicationID", applicationID);
        if (StringUtils.isNotBlank(memberID)) {
            paramMap.put("memberID", memberID);
        }
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<MemberWish> rs = memberWishService.list(paramMap, pageable);
        List<MemberWishListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), MemberWishListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getMemberWishList success!");
    }
}
