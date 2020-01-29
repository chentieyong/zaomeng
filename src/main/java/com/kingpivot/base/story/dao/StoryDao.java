package com.kingpivot.base.story.dao;

import com.kingpivot.base.story.model.Story;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "story")
@Qualifier("storyDao")
public interface StoryDao extends BaseDao<Story, String> {
}
