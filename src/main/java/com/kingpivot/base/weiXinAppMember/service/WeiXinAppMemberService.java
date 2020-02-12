package com.kingpivot.base.weiXinAppMember.service;

import com.kingpivot.base.weiXinAppMember.model.WeiXinAppMember;
import com.kingpivot.common.service.BaseService;

/**
 * Created by Administrator on 2018/6/27.
 */
public interface WeiXinAppMemberService extends BaseService<WeiXinAppMember, String> {
    WeiXinAppMember getWeiXinAppMemberByCode(String code);
}
