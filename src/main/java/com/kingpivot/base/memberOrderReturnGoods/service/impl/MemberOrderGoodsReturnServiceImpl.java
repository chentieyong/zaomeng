package com.kingpivot.base.memberOrderReturnGoods.service.impl;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberOrderGoods.dao.MemberOrderGoodsDao;
import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.base.memberOrderReturnGoods.dao.MemberOrderGoodsReturnDao;
import com.kingpivot.base.memberOrderReturnGoods.model.MemberOrderGoodsReturn;
import com.kingpivot.base.memberOrderReturnGoods.service.MemberOrderGoodsReturnService;
import com.kingpivot.base.sequenceDefine.service.SequenceDefineService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service("memberOrderGoodsReturnService")
public class MemberOrderGoodsReturnServiceImpl extends BaseServiceImpl<MemberOrderGoodsReturn, String> implements MemberOrderGoodsReturnService {
    @Resource(name = "memberOrderGoodsReturnDao")
    private MemberOrderGoodsReturnDao memberOrderGoodsReturnDao;
    @Autowired
    private MemberOrderGoodsDao memberOrderGoodsDao;
    @Autowired
    private SequenceDefineService sequenceDefineService;

    @Override
    public BaseDao<MemberOrderGoodsReturn, String> getDAO() {
        return this.memberOrderGoodsReturnDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void memberOrderGoodsReturn(MemberOrderGoods memberOrderGoods, Member member, String description) {
        memberOrderGoods.setStatus(2);
        memberOrderGoods.setModifiedTime(new Timestamp(System.currentTimeMillis()));
        memberOrderGoodsDao.save(memberOrderGoods);
        MemberOrderGoodsReturn memberOrderGoodsReturn = new MemberOrderGoodsReturn();
        memberOrderGoodsReturn.setName(String.format("%s申请退货", memberOrderGoods.getName()));
        memberOrderGoodsReturn.setMemberOrderGoodsID(memberOrderGoods.getId());
        memberOrderGoodsReturn.setMemberID(member.getId());
        memberOrderGoodsReturn.setApplicationID(member.getApplicationID());
        memberOrderGoodsReturn.setReturnCode(sequenceDefineService.genCode("orderSeq", memberOrderGoodsReturn.getId()));
        memberOrderGoodsReturn.setApplyTime(memberOrderGoods.getModifiedTime());
        memberOrderGoodsReturn.setDescription(description);
        memberOrderGoodsReturn.setGoodsShopID(memberOrderGoods.getGoodsShopID());
        if (StringUtils.isNotBlank(memberOrderGoods.getObjectFeatureItemID1())) {
            memberOrderGoodsReturn.setObjectFeatureItemID1(memberOrderGoods.getObjectFeatureItemID1());
        }
        memberOrderGoodsReturn.setQty(memberOrderGoods.getQTY());
        memberOrderGoodsReturn.setPriceTotalReturn(memberOrderGoods.getPriceTotalReturn());
        memberOrderGoodsReturn.setCreatedTime(memberOrderGoodsReturn.getApplyTime());
        memberOrderGoodsReturnDao.save(memberOrderGoodsReturn);
    }
}
