package com.jxlg.app.store.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.dao.CategoryMapper;
import com.jxlg.app.store.entity.Category;
import com.jxlg.app.store.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author zhouboxi
 * @create 2017-12-02 16:19
 **/
@Service
public class CategoryServiceImpl implements ICategoryService{

    private Logger logger =LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    //对分类的添加
    @Override
    public ServerResponse<Category> add(Integer parentId, String categoryName) {
        if(parentId==null|| StringUtils.isBlank(categoryName)){
            return ServerResponse.createByERRORMsg("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        //表示可用状态
        category.setStatus(true);
        int insert = categoryMapper.insert(category);
        if(insert>0){
            return ServerResponse.createBySuccessMsg("添加成功!");
        }
        return ServerResponse.createByERRORMsg("添加失败!");
    }


    //对分类的修改
    @Override
    public ServerResponse<String> update(Integer categoryId, String categoryName) {
        if(categoryId==null ||StringUtils.isBlank(categoryName)){
            return  ServerResponse.createByERRORMsg("参数异常!");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int i = categoryMapper.updateByPrimaryKeySelective(category);
        if(i>0){
            return  ServerResponse.createBySuccessMsg("更新成功!");
        }
        return ServerResponse.createByERRORMsg("更新失败!");
    }

    //得到所有的子节点
    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.getAllByParentId(categoryId);
        //对集合的非空判断
        if(CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前子分类");
        }
        return ServerResponse.createBySuccess(categoryList);

    }


    //递归查询本节点的id及孩子节点的id
    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        //guava的set集合
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        //guava的list集合 保存所有的id
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId!=null){
            for (Category category : categorySet) {
                categoryIdList.add(category.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //递归算法,算出子节点

    private Set<Category> findChildCategory(Set<Category> categorySet ,Integer categoryId){
        //通过id找到主分类
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
        // 如果能查得到吧它存放到一个set集合里面去
            categorySet.add(category);
        }
        //查找子节点,递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryMapper.getAllByParentId(categoryId);
        //退出条件是categoryList为空
        for (Category categorys : categoryList) {
            findChildCategory(categorySet,categorys.getId());
        }
        return  categorySet;

    }
}

