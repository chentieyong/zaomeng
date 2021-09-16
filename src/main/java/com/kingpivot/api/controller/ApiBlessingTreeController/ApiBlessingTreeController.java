package com.kingpivot.api.controller.ApiBlessingTreeController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.blessingTree.BlessingTreeDetailListDto;
import com.kingpivot.api.dto.blessingTree.BlessingTreeListDto;
import com.kingpivot.api.dto.goodsShop.GoodsShopListDto;
import com.kingpivot.base.blessingTree.model.BlessingTree;
import com.kingpivot.base.blessingTree.service.BlessingTreeService;
import com.kingpivot.base.blessingTreeDetail.model.BlessingTreeDetail;
import com.kingpivot.base.blessingTreeDetail.service.BlessingTreeDetailService;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.utils.ApiPageUtil;
import com.kingpivot.common.utils.BeanMapper;
import com.kingpivot.common.utils.TPage;
import com.kingpivot.common.utils.TimeTest;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "祈福树接口")
public class ApiBlessingTreeController extends ApiBaseController {

    @Autowired
    private BlessingTreeService blessingTreeService;
    @Autowired
    private BlessingTreeDetailService blessingTreeDetailService;

    @ApiOperation(value = "getBlessingTreeList", notes = "获取祈福树列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "memberID", value = "会员id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getBlessingTreeList")
    public MessagePacket getBlessingTreeList(HttpServletRequest request) {
        String applicationID = request.getParameter("applicationID");
        if (StringUtils.isEmpty(applicationID)) {
            return MessagePacket.newFail(MessageHeader.Code.applicationIdIsNull, "applicationID不能为空");
        }

        Timestamp nowTime = TimeTest.getTime();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        paramMap.put("beginTime:lte", nowTime);
        paramMap.put("endTime:gte", nowTime);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "beginTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<BlessingTree> rs = blessingTreeService.list(paramMap, pageable);

        List<BlessingTreeListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), BlessingTreeListDto.class);
            Map<String, Object> param = null;
            List<BlessingTreeDetail> detailList = null;
            for (BlessingTreeListDto data : list) {
                param = new HashMap<>();
                param.put("isValid", Constants.ISVALID_YES);
                param.put("isLock", Constants.ISLOCK_NO);
                param.put("blessingTreeID", data.getId());
                detailList = blessingTreeDetailService.list(param);
                if (!detailList.isEmpty()) {
                    data.setBlessingTreeDetailList(BeanMapper.mapList(detailList, BlessingTreeDetailListDto.class));
                }
            }
            page.setTotalSize((int) rs.getTotalElements());
        }

        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getBlessingTreeList success!");
    }
}
