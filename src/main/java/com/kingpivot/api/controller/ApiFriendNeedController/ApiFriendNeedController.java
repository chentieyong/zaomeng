package com.kingpivot.api.controller.ApiFriendNeedController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.friendNeed.FriendNeedDetailDto;
import com.kingpivot.api.dto.friendNeed.FriendNeedListDto;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.friendNeed.model.FriendNeed;
import com.kingpivot.base.friendNeed.service.FriendNeedService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.pointapplication.service.PointApplicationService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.KingBase;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.attachment.AddAttachmentDto;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.jms.dto.point.UsePointRequest;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "交友需求管理接口")
public class ApiFriendNeedController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private FriendNeedService friendNeedService;
    @Autowired
    private KingBase kingBase;
    @Autowired
    private PointApplicationService pointApplicationService;

    @ApiOperation(value = "submitOneFriendNeed", notes = "提交一个交友需求")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "age", value = "年龄", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "titleID", value = "性别", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "salaryCategoryID", value = "收入范围", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "email", value = "邮箱", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "urls", value = "附件图", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneFriendNeed")
    public MessagePacket submitOneFriendNeed(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        if (StringUtils.isEmpty(sessionID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        Member member = (Member) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBER_KEY.key, sessionID));
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        MemberLogDTO memberLogDTO = (MemberLogDTO) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBERLOG_KEY.key, sessionID));
        if (memberLogDTO == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }

        //积分是否足够
        if(!kingBase.pointLess(member, pointApplicationService.getNumberByAppIdAndPointName(
                member.getApplicationID(), Config.FRIENDNEED_POINT_USENAME))){
            return MessagePacket.newFail(MessageHeader.Code.pointNumberLess, "积分个数不足");
        }

        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "name不能为空");
        }
        String description = request.getParameter("description");//说明
        String beginDate = request.getParameter("beginDate");//开始日期
        String endDate = request.getParameter("endDate");//结束日期
        String days = request.getParameter("days");//发布在线天数
        String age = request.getParameter("age");
        String titleID = request.getParameter("titleID");//性别
        String salaryCategoryID = request.getParameter("salaryCategoryID");//收入范围
        String phone = request.getParameter("phone");//电话
        String email = request.getParameter("email");//邮箱
        String address = request.getParameter("address");//地址
        String urls = request.getParameter("urls");//附件图

        FriendNeed friendNeed = new FriendNeed();
        friendNeed.setApplicationID(member.getApplicationID());
        friendNeed.setName(name);
        friendNeed.setDescription(description);
        friendNeed.setMemberID(member.getId());
        if (StringUtils.isEmpty(beginDate)) {
            friendNeed.setBeginDate(new Timestamp(System.currentTimeMillis()));
        } else {
            friendNeed.setBeginDate(TimeTest.strToDate(beginDate));
        }
        if (StringUtils.isNotBlank(endDate)) {
            friendNeed.setEndDate(TimeTest.strToDate(endDate));
        } else if (StringUtils.isNotBlank(days)) {
            friendNeed.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), Integer.parseInt(days)));
        } else {
            friendNeed.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), 7));
        }
        if (StringUtils.isNotBlank(age)) {
            friendNeed.setAge(Integer.parseInt(age));
        }
        if (StringUtils.isNotBlank(titleID)) {
            friendNeed.setTitleID(titleID);
        }
        if (StringUtils.isNotBlank(email)) {
            friendNeed.setEmail(email);
        }
        if (StringUtils.isNotBlank(salaryCategoryID)) {
            friendNeed.setSalaryCategoryID(salaryCategoryID);
        }
        if (StringUtils.isNotBlank(phone)) {
            friendNeed.setPhone(phone);
        }
        if (StringUtils.isNotBlank(address)) {
            friendNeed.setAddress(address);
        }
        friendNeedService.save(friendNeed);

        if (StringUtils.isNotBlank(urls)) {
            //新增附件图
            sendMessageService.sendAddAttachmentMessage(JacksonHelper.toJson(new AddAttachmentDto.Builder()
                    .objectID(friendNeed.getId())
                    .objectDefineID(Config.FRIENDNEED_OBJECTDEFIPOST)
                    .objectName(friendNeed.getName())
                    .fileType(1)
                    .showType(1)
                    .urls(urls)
                    .build()));
        }

        //发送消耗积分
        sendMessageService.sendUsePointMessage(JacksonHelper.toJson(new UsePointRequest.Builder()
                .objectDefineID(Config.FRIENDNEED_OBJECTDEFIPOST)
                .memberID(member.getId())
                .pointName(Config.FRIENDNEED_POINT_USENAME)
                .build()));

        String desc = String.format("%s提交一个交友需求", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEFRIENDNEED.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", friendNeed.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneFriendNeed success!");
    }

    @ApiOperation(value = "getFriendNeedList", notes = "获取交友需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getFriendNeedList")
    public MessagePacket getFriendNeedList(HttpServletRequest request) {
        String applicationID = request.getParameter("applicationID");
        if (StringUtils.isEmpty(applicationID)) {
            return MessagePacket.newFail(MessageHeader.Code.applicationIdIsNull, "applicationID不能为空");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("applicationID", applicationID);
        paramMap.put("endDate:gte", new Timestamp(System.currentTimeMillis()));
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<FriendNeed> rs = friendNeedService.list(paramMap, pageable);
        List<FriendNeedListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), FriendNeedListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getFriendNeedList success!");
    }

    @ApiOperation(value = "getFriendNeedDetail", notes = "获取交友需求详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "friendNeedID", value = "交友需求id", dataType = "String")})
    @RequestMapping(value = "/getFriendNeedDetail")
    public MessagePacket getFriendNeedDetail(HttpServletRequest request) {
        String friendNeedID = request.getParameter("friendNeedID");
        if (StringUtils.isEmpty(friendNeedID)) {
            return MessagePacket.newFail(MessageHeader.Code.friendNeedIDIsNull, "friendNeedID不能为空");
        }
        FriendNeed friendNeed = friendNeedService.findById(friendNeedID);
        if (friendNeed == null) {
            return MessagePacket.newFail(MessageHeader.Code.friendNeedIDIsError, "friendNeedID不正确");
        }
        FriendNeedDetailDto data = BeanMapper.map(friendNeed, FriendNeedDetailDto.class);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);
        return MessagePacket.newSuccess(rsMap, "getFriendNeedDetail success!");
    }
}
