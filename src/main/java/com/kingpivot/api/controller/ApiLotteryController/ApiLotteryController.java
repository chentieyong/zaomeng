package com.kingpivot.api.controller.ApiLotteryController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.lottery.LotteryDetailDto;
import com.kingpivot.api.dto.lottery.lotteryListDto;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.lottery.model.Lottery;
import com.kingpivot.base.lottery.service.LotteryService;
import com.kingpivot.base.lotteryGrade.model.LotteryGrade;
import com.kingpivot.base.lotteryGrade.service.LotteryGradeService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberLottery.service.MemberLotteryService;
import com.kingpivot.base.memberRaffle.model.MemberRaffle;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.KingBase;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberBalance.MemberBalanceRequest;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.jms.dto.point.GetPointRequest;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@RequestMapping("/api")
@RestController
@Api(description = "抽奖管理接口")
public class ApiLotteryController extends ApiBaseController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private MemberLotteryService memberLotteryService;
    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LotteryGradeService lotteryGradeService;
    @Autowired
    private KingBase kingBase;

    @ApiOperation(value = "getLotteryList", notes = "抽奖列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getLotteryList")
    public MessagePacket getLotteryList(HttpServletRequest request) {
        String applicationID = request.getParameter("applicationID");
        if (StringUtils.isEmpty(applicationID)) {
            return MessagePacket.newFail(MessageHeader.Code.applicationIdIsNull, "applicationID不能为空");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("applicationID", applicationID);
        paramMap.put("beginTime:lte", new Timestamp(System.currentTimeMillis()));
        paramMap.put("endTime:gte", new Timestamp(System.currentTimeMillis()));
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<Lottery> rs = lotteryService.list(paramMap, pageable);
        List<lotteryListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), lotteryListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getLotteryList success!");
    }

    @ApiOperation(value = "getLotteryDetail", notes = "抽奖详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "lotteryID", value = "抽奖id", dataType = "String")})
    @RequestMapping(value = "/getLotteryDetail")
    public MessagePacket getLotteryDetail(HttpServletRequest request) {
        String lotteryID = request.getParameter("lotteryID");
        if (StringUtils.isEmpty(lotteryID)) {
            return MessagePacket.newFail(MessageHeader.Code.lotteryIDIsNull, "lotteryID不能为空");
        }
        String memberID = request.getParameter("memberID");
        Lottery lottery = lotteryService.findById(lotteryID);
        if (lottery == null) {
            return MessagePacket.newFail(MessageHeader.Code.lotteryIDIsError, "lotteryID不正确");
        }
        LotteryDetailDto data = BeanMapper.map(lottery, LotteryDetailDto.class);
        if (StringUtils.isNotBlank(memberID)) {
            data.setMyJoinTime(memberLotteryService.getJoinTimeByLotteryIDAndMemberID(lotteryID, memberID));
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);
        return MessagePacket.newSuccess(rsMap, "getLotteryDetail success!");
    }

    @ApiOperation(value = "joinOneLottery", notes = "参加一个抽奖")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "lotteryID", value = "抽奖id", dataType = "String")})
    @RequestMapping(value = "/joinOneLottery")
    public MessagePacket joinOneLottery(HttpServletRequest request) {
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
        String lotteryID = request.getParameter("lotteryID");
        if (StringUtils.isEmpty(lotteryID)) {
            return MessagePacket.newFail(MessageHeader.Code.lotteryIDIsNull, "lotteryID不能为空");
        }
        Lottery lottery = lotteryService.findById(lotteryID);
        if (lottery == null) {
            return MessagePacket.newFail(MessageHeader.Code.lotteryIDIsError, "lotteryID不正确");
        }
        if (lottery.getBeginTime() == null || lottery.getEndTime() == null) {
            return MessagePacket.newFail(MessageHeader.Code.lotteryTimeError, "抽奖时间异常");
        }
        Timestamp curTime = new Timestamp(System.currentTimeMillis());
        if (lottery.getBeginTime().getTime() > curTime.getTime()
                || lottery.getEndTime().getTime() < curTime.getTime()) {
            return MessagePacket.newFail(MessageHeader.Code.lotteryTimeError, "不在抽奖时间范围内");
        }
        List<LotteryGrade> lotteryGradeList = lotteryGradeService.getLotteryGradeByLotteryID(lotteryID);
        if (lotteryGradeList == null || lotteryGradeList.size() == 0) {
            return MessagePacket.newFail(MessageHeader.Code.lotteryGradeIsEmpty, "抽奖等级数据为空");
        }

        //统计奖品总数
        int total = 0;
        List<Integer> codeList = new ArrayList<>();
        codeList.add(0);
        for (LotteryGrade lotteryGrade : lotteryGradeList) {
            total += lotteryGrade.getRaffleRate();
            codeList.add(lotteryGrade.getRaffleRate());
        }

        //产生伪随机数
        final int myCode = (int) (Math.random() * total);

        logger.info("myCode=[{}]", myCode);
        logger.info("codeList=[{}]", Arrays.toString(codeList.toArray()));

        //判断是否中奖
        LotteryGrade lotteryGrade = null;
        int curTotal = 0;
        int nextTotal = 0;
        for (int i = 1; i < codeList.size(); i++) {
            curTotal = nextTotal;
            nextTotal = curTotal + codeList.get(i).intValue();
            if (myCode >= curTotal && myCode <= nextTotal) {
                lotteryGrade = lotteryGradeList.get(i - 1);
                break;
            }
        }

        if (lotteryGrade.getNumber() < lotteryGrade.getGetNumber() + 1) {
            lotteryGrade = lotteryGradeList.get(lotteryGradeList.size() - 1);
        }

        lotteryGrade.setGetNumber(lotteryGrade.getGetNumber() + 1);
        lotteryGradeService.save(lotteryGrade);

        if (lotteryGrade.getRaffle().getPoint() != 0) {
            logger.info("获奖云积分=[{}]", lotteryGrade.getRaffle().getPoint());
            //队列发放云积分
            sendMessageService.sendUsePointMessage(JacksonHelper.toJson(new GetPointRequest.Builder()
                    .objectDefineID(Config.LOTTERY_OBJECTDEFINEID)
                    .memberID(member.getId())
                    .pointName(Config.JOINLOTTERY_GET_POINT_USENAME)
                    .build()));

            //添加中奖记录
            kingBase.addMemberRaffle(member, lotteryGrade, 3, 2);
            //添加会员抽奖记录
            kingBase.addMemberLottery(member, lottery, String.valueOf(myCode), 2);
        } else if (lotteryGrade.getRaffle().getCash() != 0) {
            logger.info("获奖润积分=[{}]", lotteryGrade.getRaffle().getCash());
            //队列发放润积分
            sendMessageService.sendMemberBalance(JacksonHelper.toJson(new MemberBalanceRequest.Builder()
                    .memberID(member.getId())
                    .applicationID(member.getApplicationID())
                    .siteID(memberLogDTO.getSiteId())
                    .operateType(2)
                    .objectDefineID(Config.LOTTERY_OBJECTDEFINEID)
                    .objectName(lottery.getName())
                    .objectID(lottery.getId())
                    .amount(new BigDecimal(lotteryGrade.getRaffle().getCash()))
                    .description(String.format("%s抽奖获取润余额", member.getName()))
                    .type(2)
                    .build()));

            //添加中奖记录
            kingBase.addMemberRaffle(member, lotteryGrade, 3, 2);
            //添加会员抽奖记录
            kingBase.addMemberLottery(member, lottery, String.valueOf(myCode), 2);
        }

        String desc = String.format("%s参加一个抽奖", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.JOINONELOTTERY.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", lotteryGrade.getName());
        return MessagePacket.newSuccess(rsMap, "joinOneLottery success!");
    }

}
