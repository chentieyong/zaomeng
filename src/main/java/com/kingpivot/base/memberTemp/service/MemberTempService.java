package com.kingpivot.base.memberTemp.service;

import com.kingpivot.base.memberTemp.model.MemberTemp;
import com.kingpivot.common.service.BaseService;

import java.util.List;

public interface MemberTempService extends BaseService<MemberTemp, String> {
    List<MemberTemp> getList();
}
