package com.kingpivot.base.category.service.impl;

import com.kingpivot.base.category.dao.CategoryDao;
import com.kingpivot.base.category.model.Category;
import com.kingpivot.base.category.service.CategoryService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import com.kingpivot.common.util.TreeDataUtil;
import com.kingpivot.common.util.TreeInfo;
import com.kingpivot.common.util.TreeInfoDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl extends BaseServiceImpl<Category, String> implements CategoryService {
    @Resource(name = "categoryDao")
    private CategoryDao categoryDao;

    @Override
    public BaseDao<Category, String> getDAO() {
        return this.categoryDao;
    }

    @Override
    public TreeInfoDTO<TreeInfo> getTreeData(String rootId, String depth) {
        Object[] list = categoryDao.getTreeData(rootId, depth);
        List<TreeInfo> treeInfoList = new ArrayList<>();
        TreeInfo treeInfo = null;
        Object[] obj = null;
        for (int i = 0; i < list.length; i++) {
            obj = (Object[]) list[i];
            treeInfo = new TreeInfo();
            treeInfo.setId((String) obj[0]);
            treeInfo.setpId((String) obj[1]);
            treeInfo.setName((String) obj[2]);
            treeInfo.setSmallIcon((String) obj[3]);
            treeInfo.setLargeIcon((String) obj[4]);
            treeInfo.setDepth((Integer) obj[5]);
            BigInteger orderSeq = (BigInteger) obj[6];
            if (orderSeq != null) {
                treeInfo.setOrderSeq(orderSeq.intValue());
            }
            treeInfo.setIsLeaf((Integer) obj[7]);
            treeInfoList.add(treeInfo);
        }
        if (!treeInfoList.isEmpty()) {
            TreeDataUtil treeDataUtil = new TreeDataUtil(treeInfoList);
            TreeInfoDTO<TreeInfo> treeInfoDTO = new TreeInfoDTO<>(treeInfoList.size(), treeDataUtil.getMaxDepth(), treeDataUtil.recursiveTree(rootId));
            return treeInfoDTO;
        } else {
            return null;
        }
    }

    @Override
    public String getIdByNameAndAppId(String name, String applicationID) {
        return categoryDao.getIdByNameAndAppId(name, applicationID);
    }
}
