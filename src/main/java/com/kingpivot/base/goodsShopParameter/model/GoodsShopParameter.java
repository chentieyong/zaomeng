package com.kingpivot.base.goodsShopParameter.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "goodsShopParameter")
public class GoodsShopParameter extends BaseModel<String> {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 36)
    private String companyID;//公司ID
    @Column(length = 36)
    private String goodsShopID;//店铺ID
    @Column(length = 36)
    private String goodsShopParameterDefineID;//产品参数定义ID
    @Column(length = 100)
    private String name;//名称
    @Column(length = 20)
    private String value;//值
    @Column()
    private Integer orderSeq;//序号

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getGoodsShopID() {
        return goodsShopID;
    }

    public void setGoodsShopID(String goodsShopID) {
        this.goodsShopID = goodsShopID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGoodsShopParameterDefineID() {
        return goodsShopParameterDefineID;
    }

    public void setGoodsShopParameterDefineID(String goodsShopParameterDefineID) {
        this.goodsShopParameterDefineID = goodsShopParameterDefineID;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }
}
