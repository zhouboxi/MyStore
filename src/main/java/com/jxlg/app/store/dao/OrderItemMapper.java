package com.jxlg.app.store.dao;

import com.jxlg.app.store.annotation.MybatisRepository;
import com.jxlg.app.store.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@MybatisRepository
public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    void batchInsert(@Param("orderItems")List<OrderItem> orderItems);

    List<OrderItem> getByOrderNoUserId(@Param("orderNo")Long orderNo,@Param("userId")Integer userId);

    List<OrderItem> getByOrderNo(Long orderNo);
}