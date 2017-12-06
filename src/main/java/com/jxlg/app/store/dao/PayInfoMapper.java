package com.jxlg.app.store.dao;

import com.jxlg.app.store.annotation.MybatisRepository;
import com.jxlg.app.store.entity.PayInfo;

@MybatisRepository
public interface PayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayInfo record);

    int insertSelective(PayInfo record);

    PayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayInfo record);

    int updateByPrimaryKey(PayInfo record);
}