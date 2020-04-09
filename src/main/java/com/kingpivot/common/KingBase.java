package com.kingpivot.common;

import com.kingpivot.base.cart.model.Cart;
import com.kingpivot.base.cart.service.CartService;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.lottery.model.Lottery;
import com.kingpivot.base.lotteryGrade.model.LotteryGrade;
import com.kingpivot.base.lotteryGrade.service.LotteryGradeService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberLottery.model.MemberLottery;
import com.kingpivot.base.memberLottery.service.MemberLotteryService;
import com.kingpivot.base.memberPayment.model.MemberPayment;
import com.kingpivot.base.memberPayment.service.MemberPaymentService;
import com.kingpivot.base.memberRaffle.model.MemberRaffle;
import com.kingpivot.base.memberRaffle.service.MemberRaffleService;
import com.kingpivot.base.memberstatistics.model.MemberStatistics;
import com.kingpivot.base.memberstatistics.service.MemberStatisticsService;
import com.kingpivot.base.sequenceDefine.service.SequenceDefineService;
import com.kingpivot.base.sms.model.SMS;
import com.kingpivot.base.sms.service.SMSService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberBalance.MemberBalanceRequest;
import com.kingpivot.common.jms.dto.point.GetPointRequest;
import com.kingpivot.common.utils.JacksonHelper;
import com.kingpivot.common.utils.TimeTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by apple on 15/12/17.
 */
@Component
public class KingBase {
    @Autowired
    private CartService cartService;
    @Autowired
    private SMSService smsService;
    @Autowired
    private MemberPaymentService memberPaymentService;
    @Autowired
    private SequenceDefineService sequenceDefineService;
    @Autowired
    private MemberStatisticsService memberStatisticsService;
    @Autowired
    private MemberRaffleService memberRaffleService;
    @Autowired
    private MemberLotteryService memberLotteryService;
    @Autowired
    private LotteryGradeService lotteryGradeService;
    @Resource
    private SendMessageService sendMessageService;

    private static final Logger logger = LoggerFactory.getLogger(KingBase.class);

    /**
     * 添加默认购物车
     *
     * @param member
     * @return
     */
    public String insertCart(Member member) {
        Cart cart = new Cart();
        cart.setName(String.format("%s的购物车", member.getName()));
        cart.setMemberID(member.getId());
        cartService.save(cart);
        return cart.getId();
    }

    /**
     * 添加sms记录
     *
     * @param name
     * @param shortName
     * @param content
     * @param phone
     * @param smsWayID
     */
    public void addSms(String name, String shortName, String content,
                       String phone, String smsWayID) {
        SMS sms = new SMS();
        sms.setContent(content);
        sms.setName(name);
        sms.setShortName(shortName);
        sms.setReceiverNumber(phone);
        sms.setSmsWayID(smsWayID);
        sms.setSendDate(new Timestamp(System.currentTimeMillis()));
        smsService.save(sms);
    }

    /**
     * 添加支付日志
     *
     * @param member
     * @param objectDefineID
     * @param amount
     * @return
     */
    public MemberPayment addMemberPayment(Member member, String objectDefineID, double amount) {
        MemberPayment memberPayment = new MemberPayment();
        memberPayment.setName(String.format("%s%s申请支付", member.getName(), TimeTest.getNowDateFormat()));
        memberPayment.setMemberID(member.getId());
        memberPayment.setObjectDefineID(objectDefineID);
        memberPayment.setApplicationID(member.getApplicationID());
        memberPayment.setAmount(amount);
        memberPayment.setApplyTime(new Timestamp(System.currentTimeMillis()));
        memberPayment.setOrderCode(sequenceDefineService.genCode("orderSeq", memberPayment.getId()));
        memberPayment.setStatus(1);
        memberPayment = memberPaymentService.save(memberPayment);
        return memberPayment;
    }

    /**
     * 校验积分是否足够
     *
     * @param member
     * @param number
     * @return
     */
    public boolean pointLess(Member member, int number) {
        int point = memberStatisticsService.getMemberPoint(member.getId());
        if (point < number) {
            return false;
        }
        return true;
    }

    public void addMemberRaffle(Member member, LotteryGrade lotteryGrade, int giveType, int status) {
        MemberRaffle memberRaffle = new MemberRaffle();
        memberRaffle.setApplicationID(member.getApplicationID());
        memberRaffle.setName(String.format("会员%s中奖", member.getName()));
        memberRaffle.setRaffleID(lotteryGrade.getRaffleID());
        memberRaffle.setMemberID(member.getId());
        memberRaffle.setGiveType(giveType);
        if (status == 2) {
            memberRaffle.setGiveTime(new Timestamp(System.currentTimeMillis()));
        }
        memberRaffle.setStatus(status);
        memberRaffleService.save(memberRaffle);
    }

    public void addMemberLottery(Member member, Lottery lottery, String myCode, int resultType) {
        MemberLottery memberLottery = new MemberLottery();
        memberLottery.setName(String.format("会员%s抽奖", member.getName()));
        memberLottery.setApplicationID(member.getApplicationID());
        memberLottery.setMemberID(member.getId());
        memberLottery.setJoinTime(new Timestamp(System.currentTimeMillis()));
        memberLottery.setMyCode(myCode);
        if (resultType == 2) {
            memberLottery.setGiveTime(new Timestamp(System.currentTimeMillis()));
        }
        memberLottery.setLotteryID(lottery.getId());
        memberLottery.setResultType(resultType);
        memberLotteryService.save(memberLottery);
    }

    public String joinOneLottery(Lottery lottery, LotteryGrade lotteryGrade,
                                 Member member, MemberLogDTO memberLogDTO, String myCode) {
        lotteryGrade.setGetNumber(lotteryGrade.getGetNumber() + 1);
        lotteryGradeService.save(lotteryGrade);

        if (lotteryGrade.getRaffle().getPoint() != 0) {
            logger.info("获奖云积分=[{}]", lotteryGrade.getRaffle().getPoint());
            //队列发放云积分
            sendMessageService.sendGetPointMessage(JacksonHelper.toJson(new GetPointRequest.Builder()
                    .objectDefineID(Config.LOTTERY_OBJECTDEFINEID)
                    .memberID(member.getId())
                    .pointName(Config.JOINLOTTERY_GET_POINT_USENAME)
                    .point(lotteryGrade.getRaffle().getPoint())
                    .build()));
            //添加中奖记录
            addMemberRaffle(member, lotteryGrade, 3, 2);
            //添加会员抽奖记录
            addMemberLottery(member, lottery, myCode, 2);
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
            addMemberRaffle(member, lotteryGrade, 3, 2);
            //添加会员抽奖记录
            addMemberLottery(member, lottery, myCode, 2);
        }
        return lotteryGrade.getName();
    }
}
