package com.jxlg.app.store.dao;

import com.jxlg.app.store.annotation.MybatisRepository;
import com.jxlg.app.store.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MybatisRepository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart checkCart(@Param("userId")Integer userId,@Param("productId")Integer productId);

    int delete(@Param("userId")Integer userId,@Param("productIds")List<String> productIds);

    List<Cart> findByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int checkedOrUncheckedProduct(@Param("userId")Integer userId,@Param("productId")Integer productId,@Param("checked") Integer checked);

    int selectCartProductCount(Integer userId);

    List<Cart> selectCheckedCartByUserId(Integer userId);
}