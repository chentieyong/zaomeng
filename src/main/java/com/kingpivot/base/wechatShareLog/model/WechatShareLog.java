package com.kingpivot.base.wechatShareLog.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.wechart.model.Wechart;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "wechatShareLog")
public class WechatShareLog extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;//名称

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column(length = 36)
    private String wechartID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wechartID", insertable = false, updatable = false)  //不能保存和修改
    private Wechart wechart;

    @Column(length = 36)
    private String memberID;//会员ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;

    @Column(length = 200)
    private String url;//url

    @Column(length = 20)
    private String type;//类型 1好友 2朋友圈

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getWechartID() {
        return wechartID;
    }

    public void setWechartID(String wechartID) {
        this.wechartID = wechartID;
    }

    public Wechart getWechart() {
        return wechart;
    }

    public void setWechart(Wechart wechart) {
        this.wechart = wechart;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
