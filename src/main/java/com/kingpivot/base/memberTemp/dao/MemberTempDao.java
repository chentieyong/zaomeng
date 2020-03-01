package com.kingpivot.base.memberTemp.dao;

import com.kingpivot.base.memberTemp.model.MemberTemp;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Repository
@Table(name = "member_temp")
@Qualifier("memberTempDao")
public interface MemberTempDao extends BaseDao<MemberTemp, String> {
    @Query(value = "SELECT * FROM member_temp ORDER BY createdTime asc", nativeQuery = true)
    List<MemberTemp> getList();
}
