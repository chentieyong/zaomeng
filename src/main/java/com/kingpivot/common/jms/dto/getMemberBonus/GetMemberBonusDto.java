package com.kingpivot.common.jms.dto.getMemberBonus;

public class GetMemberBonusDto {
    private String memberID;
    private String bonusDefineID;

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getBonusDefineID() {
        return bonusDefineID;
    }

    public void setBonusDefineID(String bonusDefineID) {
        this.bonusDefineID = bonusDefineID;
    }

    public GetMemberBonusDto() {

    }

    public GetMemberBonusDto(String memberID, String bonusDefineID) {
        this.memberID = memberID;
        this.bonusDefineID = bonusDefineID;
    }
}
