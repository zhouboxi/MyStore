package com.jxlg.app.store.service;

import com.github.pagehelper.PageInfo;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.entity.Shipping;

/**
 * @author zhouboxi
 * @create 2017-12-05 11:34
 **/
public interface IAddressService {
    //对地址添加

    ServerResponse add(Integer userId, Shipping shipping);
    //对地址的删除

    ServerResponse<String>  delete(Integer userId,Integer addressId);
    //对地址的修改

    ServerResponse<String> update(Integer userId,Shipping shipping);
    //对地址的查询

    ServerResponse<Shipping> findOne(Integer userId,Integer addressId);
    //对所有的地址就行查询

    ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
