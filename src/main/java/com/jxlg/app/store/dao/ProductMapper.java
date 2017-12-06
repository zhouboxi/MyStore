package com.jxlg.app.store.dao;

import com.jxlg.app.store.annotation.MybatisRepository;
import com.jxlg.app.store.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@MybatisRepository
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKeyWithBLOBs(Product record);

    int updateByPrimaryKey(Product record);

    //查询所有

    List<Product> findAll();

    //模糊查询

    List<Product> findByNameAndId(@Param("productName") String productName,@Param("productId") Integer productId);

    List<Product> selectByNameAndCategoryIds(@Param("productName")String productName, @Param("categoryIdList")List<Integer> categoryIdList);

}