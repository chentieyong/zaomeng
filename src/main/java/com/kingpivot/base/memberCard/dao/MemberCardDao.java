package com.kingpivot.base.memberCard.dao;

import com.kingpivot.base.memberCard.model.MemberCard;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberCard")
@Qualifier("memberCardDao")
public interface MemberCardDao extends BaseDao<MemberCard, String> {

    @Query(value = "SELECT * FROM memberCard WHERE isValid=1 AND isLock=0 AND memberID=?1 NOW() BETWEEN beginTime AND endTime ORDER BY createdTime DESC", nativeQuery = true)
    MemberCard getEffectiveMemberCard(String memberID);
}
