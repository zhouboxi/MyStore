package com.jxlg.app.store.dao;

import com.jxlg.app.store.annotation.MybatisRepository;
import com.jxlg.app.store.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@MybatisRepository
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order findByUserIdAndNo(@Param("userId") Integer userId,@Param("orderNo") Long orderNo);

    List<Order> getAllOrder(Integer userId);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectAllOrder();
}