package com.jxlg.app.store.dao;

import com.jxlg.app.store.annotation.MybatisRepository;
import com.jxlg.app.store.entity.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MybatisRepository
public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int delete(@Param("userId") Integer userId,@Param("addressId")Integer addressId);

    int update(Shipping shipping);

    Shipping findOne(@Param("userId") Integer userId, @Param("addressId")Integer addressId);

    List<Shipping> list(Integer userId);
}