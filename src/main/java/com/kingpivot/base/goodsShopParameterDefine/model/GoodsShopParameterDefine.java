package com.kingpivot.base.goodsShopParameterDefine.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "goodsShopParameterDefine")
public class GoodsShopParameterDefine extends BaseModel<String> {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 36)
    private String companyID;//公司ID
    @Column(length = 100)
    private String name;//名称

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
