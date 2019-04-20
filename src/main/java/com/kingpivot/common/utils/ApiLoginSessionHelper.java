package com.kingpivot.common.utils;

import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.support.MemberLogDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Component
public class ApiLoginSessionHelper {

    @Autowired
    private RedisTemplate redisTemplate;

    public void putCurrentMember(HttpServletRequest request, Member member) {
        if (null == member || null == request) {
            return;
        }
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        redisTemplate.opsForValue().set(String.format("%s%s", RedisKey.Key.MEMBER_KEY.key, sessionId), member, 7200, TimeUnit.SECONDS);
    }

    public Member getCurrentMember(String sessionId) {
        return (Member) redisTemplate.opsForValue().get(sessionId);
    }

    public String getCurrentMemberId(String sessionId) {
        Member member = getCurrentMember(sessionId);
        return (null == member) ? null : member.getId();
    }

    public boolean isMemberLogin(String sessionId) {
        Member member = getCurrentMember(sessionId);
        return (null != member && StringUtils.isNotBlank(member.getId()));
    }

    public void putLoginMemberDto(HttpServletRequest request, MemberLogDTO memberLogDTO) {
        if (null == memberLogDTO || null == request) {
            return;
        }
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        redisTemplate.opsForValue().set(String.format("%s%s", RedisKey.Key.MEMBERLOG_KEY.key, sessionId), memberLogDTO, 7200, TimeUnit.SECONDS);
    }

    public MemberLogDTO getMemberDto(String sessionId) {
        return (MemberLogDTO) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBERLOG_KEY.key, sessionId));
    }

    public void destory(String sessionId) {
        redisTemplate.delete(sessionId);
    }
}
