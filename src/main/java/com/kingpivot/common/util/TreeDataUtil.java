package com.kingpivot.common.util;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2015/10/22.
 */
public class TreeDataUtil {

    private  List<TreeInfo> treeList;

    public TreeDataUtil(List<TreeInfo> treeList) {
        this.treeList = treeList;
    }

    public List<TreeInfo> getTreeList() {
        return treeList;
    }

    public void setTreeList(List<TreeInfo> treeList) {
        this.treeList = treeList;
    }

    /**
     * 递归算法解析成树形结构
     *
     * @param id
     * @return
     */
    public  TreeInfo recursiveTree(String id) {
        TreeInfo node = getPidTreeInfo(id);
        if(null != node){
            List<TreeInfo> childTreeNodes = getChildTreeNode(node.getId());
            for(TreeInfo child : childTreeNodes){
                if(child.getId()!=null){
                    TreeInfo n = recursiveTree(child.getId()); //递归
                    if(null == node.getNodes()){
                        node.setNodes(new ArrayList());
                    }
                    node.getNodes().add(n);
                }
            }
        }
        return node;
    }

    public  TreeInfo getPidTreeInfo(String id){
        TreeInfo treeInfo_b = null;
        for(TreeInfo treeInfo:this.treeList){
            if(id.equals(treeInfo.getId())){
                treeInfo_b = treeInfo;
                break;
            }
        }
        return treeInfo_b;
    }

    public  List<TreeInfo> getChildTreeNode(String pid){
        List<TreeInfo> list = Lists.newArrayList();
        for(TreeInfo treeInfo:this.treeList){
            if(pid.equals(treeInfo.getpId())){
                list.add(treeInfo);
            }
        }
        return list;
    }

    public Integer getMaxDepth(){
        List<Integer> depthList = Lists.newArrayList();
        for (TreeInfo treeInfo:this.treeList){
            depthList.add(treeInfo.getDepth());
        }
        Collections.sort(depthList);
        return depthList.get(depthList.size() - 1);
    }
}
