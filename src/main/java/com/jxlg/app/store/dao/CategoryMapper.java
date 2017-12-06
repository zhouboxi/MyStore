package com.jxlg.app.store.dao;

import com.jxlg.app.store.annotation.MybatisRepository;
import com.jxlg.app.store.entity.Category;

import java.util.List;
@MybatisRepository
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> getAllByParentId(Integer parentId);
}