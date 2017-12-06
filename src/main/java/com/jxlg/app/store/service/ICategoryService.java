package com.jxlg.app.store.service;

import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.entity.Category;

import java.util.List;

/**
 * @author zhouboxi
 * @create 2017-12-02 16:08
 **/
public interface ICategoryService {


    /**
     * 保存分类
     * @param parentId
     * @param categoryName
     * @return
     */
    ServerResponse<Category> add(Integer parentId, String categoryName);

    /**
     * 对分类的修改
     * @param categoryId
     * @param categoryName
     * @return
     */
    ServerResponse<String> update(Integer categoryId, String categoryName);

    /**
     * 查询所有的子分类
     * @param categoryId
     * @return
     */
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
