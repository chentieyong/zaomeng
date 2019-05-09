package com.kingpivot.base.memberOrder.dao;

import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;

@Repository
@Table(name = "memberOrder")
@Qualifier("memberOrderDao")
public interface MemberOrderDao extends BaseDao<MemberOrder, String> {
    @Query(value = "SELECT * FROM memberOrder WHERE memberPaymentID=?1 AND isValid=1 AND isLock=0",nativeQuery = true)
    List<MemberOrder> getMemberOrderByMemberPayMentID(String memberPaymentID);
}
