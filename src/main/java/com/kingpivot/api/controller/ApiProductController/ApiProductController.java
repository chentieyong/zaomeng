package com.kingpivot.api.controller.ApiProductController;

import com.google.common.collect.Maps;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.product.model.Product;
import com.kingpivot.base.product.service.ProductService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.attachment.AddAttachmentDto;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.utils.JacksonHelper;
import com.kingpivot.common.utils.TimeTest;
import com.kingpivot.common.utils.UserAgentUtil;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "热门产品管理接口")
public class ApiProductController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ProductService productService;

    @ApiOperation(value = "submitOneProduct", notes = "提交一个产品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "amount", value = "产品价格", dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "priceUnit", value = "价格单位", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deliveryFeeType", value = "邮费类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "faceImage", value = "押题图", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "listImage", value = "列表图", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "urls", value = "附件图", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneProduct")
    public MessagePacket submitOneProduct(HttpServletRequest request) {
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

        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "name不能为空");
        }
        String description = request.getParameter("description");//说明
        String beginDate = request.getParameter("beginDate");//开始日期
        String endDate = request.getParameter("endDate");//结束日期
        String amount = request.getParameter("endDate");//产品价格
        String priceUnit = request.getParameter("endDate");//价格单位
        String address = request.getParameter("endDate");//发货地址
        String deliveryFeeType = request.getParameter("endDate");//1包邮2不包邮
        String faceImage = request.getParameter("endDate");//押题图
        String listImage = request.getParameter("endDate");//列表图
        String urls = request.getParameter("urls");//附件图

        Product product = new Product();
        product.setApplicationID(member.getApplicationID());
        product.setName(name);
        product.setDescription(description);
        product.setMemberID(member.getId());
        if (StringUtils.isEmpty(beginDate)) {
            product.setBeginDate(new Timestamp(System.currentTimeMillis()));
        } else {
            product.setBeginDate(TimeTest.strToDate(beginDate));
        }
        if (StringUtils.isEmpty(endDate)) {
            product.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), 7));
        } else {
            product.setEndDate(TimeTest.strToDate(endDate));
        }
        if (StringUtils.isNotBlank(amount)) {
            product.setAmount(Double.parseDouble(amount));
        }
        if (StringUtils.isNotBlank(priceUnit)) {
            product.setPriceUnit(priceUnit);
        }
        if (StringUtils.isNotBlank(deliveryFeeType)) {
            product.setDeliveryFeeType(Integer.parseInt(deliveryFeeType));
        }
        if (StringUtils.isNotBlank(faceImage)) {
            product.setFaceImage(faceImage);
        }
        if (StringUtils.isNotBlank(listImage)) {
            product.setListImage(listImage);
        }
        if (StringUtils.isNotBlank(address)) {
            product.setAddress(address);
        }
        productService.save(product);

        if (StringUtils.isNotBlank(urls)) {
            //新增附件图
            sendMessageService.sendAddAttachmentMessage(JacksonHelper.toJson(new AddAttachmentDto.Builder()
                    .objectID(product.getId())
                    .objectDefineID(Config.PRODUCT_OBJECTDEFIPOST)
                    .objectName(product.getName())
                    .fileType(1)
                    .showType(1)
                    .urls(urls)
                    .build()));
        }

        String desc = String.format("%s提交一个产品", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEPRODUCT.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", product.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneProduct success!");
    }
}
