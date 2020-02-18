package com.kingpivot.base.major.dao;

import com.kingpivot.base.major.model.Major;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "major")
@Qualifier("majorDao")
public interface MajorDao extends BaseDao<Major, String> {
    @Query(value = "SELECT tb_major.* FROM memberMajor tb_mb_major LEFT JOIN major tb_major\n" +
            " ON tb_mb_major.majorID=tb_major.id WHERE tb_mb_major.memberID=?1 AND \n" +
            "tb_mb_major.isValid=1 AND tb_mb_major.isLock=0\n" +
            " AND tb_major.isValid=1 AND tb_major.isLock=0  ORDER BY tb_major.orderSeq desc LIMIT 1",nativeQuery = true)
    Major getMaxMemberMajorByMemberId(String memberID);
}
