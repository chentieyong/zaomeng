package com.kingpivot.base.navigator.model;

import com.kingpivot.base.application.model.Application;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "navigator")
public class Navigator extends BaseModel<String> {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "shortName", length = 100)
    private String shortName;

    @Column(name = "siteID", length = 36)
    private String siteId;

    @Column(name = "levelID")
    private Integer levelId;

    @Column(name = "showType")
    private Integer showType;

    @Column(name = "functionType")
    private Integer functionType;

    @Column(name = "functionURL", length = 100)
    private String functionUrl;

    @Column(name = "largeIcon", length = 100)
    private String largeIcon = "";

    @Column(name = "samllIcon", length = 100)
    private String samllIcon = "";

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "isLeaf", columnDefinition = "int default 0")
    private boolean isLeaf = false;

    @Column(name = "code", length = 36)
    private String code;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "orderSeq")
    private Long orderSeq;

    @Column(name = "parentID")
    private String parentID;

    @Column(name = "applicationName")
    private String applicationName;

    @Column(length = 36)
    private String companyID;//公司ID

    @Column(name = "applicationID")
    private String applicationID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicationID", insertable = false, updatable = false)  //不能保存和修改
    private Application application;

    @Column(name = "leftWeight", columnDefinition = "int default 0")
    private int leftWeight = 0;

    @Column(name = "rightWeight", columnDefinition = "int default 0")
    private int rightWeight = 0;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentID")
    @Where(clause = "isValid=1 and isLock=0")
    private List<Navigator> children;

    @Column(length = 100)
    private String cssName;//样式类型

    @Column()
    private Integer accessType;//访问权限

    @Column(columnDefinition = "int default 0")
    private Integer goodMax = 0;//精华最大个数

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public Integer getFunctionType() {
        return functionType;
    }

    public void setFunctionType(Integer functionType) {
        this.functionType = functionType;
    }

    public String getFunctionUrl() {
        return functionUrl;
    }

    public void setFunctionUrl(String functionUrl) {
        this.functionUrl = functionUrl;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String getSamllIcon() {
        return samllIcon;
    }

    public void setSamllIcon(String samllIcon) {
        this.samllIcon = samllIcon;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Long orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public int getLeftWeight() {
        return leftWeight;
    }

    public void setLeftWeight(int leftWeight) {
        this.leftWeight = leftWeight;
    }

    public int getRightWeight() {
        return rightWeight;
    }

    public void setRightWeight(int rightWeight) {
        this.rightWeight = rightWeight;
    }

    public List<Navigator> getChildren() {
        return children;
    }

    public void setChildren(List<Navigator> children) {
        this.children = children;
    }

    public String getCssName() {
        return cssName;
    }

    public void setCssName(String cssName) {
        this.cssName = cssName;
    }

    public Integer getAccessType() {
        return accessType;
    }

    public void setAccessType(Integer accessType) {
        this.accessType = accessType;
    }

    public Integer getGoodMax() {
        return goodMax;
    }

    public void setGoodMax(Integer goodMax) {
        this.goodMax = goodMax;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }
}
