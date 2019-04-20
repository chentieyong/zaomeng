package com.kingpivot.common.jms.dto.memberLogin;

public abstract class MemberLoginBaseBuilder<BuilderType, ValueType> {
    private String sessionID;
    private boolean isnew;
    private String userAgent;
    private String description;
    private String operateType;
    private String ipaddr;

    public BuilderType sessionID(String sessionID) {
        this.sessionID = sessionID;
        return (BuilderType) this;
    }

    public BuilderType isnew(boolean isnew) {
        this.isnew = isnew;
        return (BuilderType) this;
    }

    public BuilderType userAgent(String userAgent) {
        this.userAgent = userAgent;
        return (BuilderType) this;
    }

    public BuilderType description(String description) {
        this.description = description;
        return (BuilderType) this;
    }

    public BuilderType operateType(String operateType) {
        this.operateType = operateType;
        return (BuilderType) this;
    }

    public BuilderType ipaddr(String ipaddr) {
        this.ipaddr = ipaddr;
        return (BuilderType) this;
    }


    public abstract ValueType build();

    public void setBalance(MemberLoginRequestBase base) {
        base.setSessionID(this.sessionID);
        base.setIsnew(this.isnew);
        base.setUserAgent(this.userAgent);
        base.setDescription(this.description);
        base.setOperateType(this.operateType);
        base.setIpaddr(this.ipaddr);
    }
}
