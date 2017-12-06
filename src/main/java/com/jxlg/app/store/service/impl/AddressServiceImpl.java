package com.jxlg.app.store.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.dao.ShippingMapper;
import com.jxlg.app.store.entity.Shipping;
import com.jxlg.app.store.service.IAddressService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zhouboxi
 * @create 2017-12-05 11:34
 **/
@Service
public class AddressServiceImpl implements IAddressService {

    @Resource
    private ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
       //提交的时候没有用户id在这进行赋值
        shipping.setUserId(userId);
        int insert = shippingMapper.insert(shipping);
        if(insert>0){
            Map result = Maps.newHashMap();
            result.put("addressId",shipping.getId());
            return ServerResponse.createBySuccess("新建成功!",result);
        }
        return ServerResponse.createByERRORMsg("新建失败!");
    }

    @Override
    public ServerResponse<String>  delete(Integer userId, Integer addressId) {
        int i = shippingMapper.delete(userId,addressId);
        if(i>0){
            return ServerResponse.createBySuccess("删除成功!");
        }
        return ServerResponse.createByERRORMsg("删除失败!");
    }

    @Override
    public ServerResponse<String>  update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int update = shippingMapper.update(shipping);
        if(update>0){
            return  ServerResponse.createBySuccess("更新成功!");
        }
        return  ServerResponse.createByERRORMsg("更新失败!");
    }

    @Override
    public ServerResponse<Shipping> findOne(Integer userId, Integer addressId) {
        Shipping shipping = shippingMapper.findOne(userId, addressId);
        if(shipping==null){
            return  ServerResponse.createByERRORMsg("未找到数据");
        }
        return ServerResponse.createBySuccess("请求成功",shipping);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> list = shippingMapper.list(userId);
        if(CollectionUtils.isNotEmpty(list)){
            PageInfo pageInfo = new PageInfo(list);
            return ServerResponse.createBySuccess(pageInfo);
        }
        return ServerResponse.createByERRORMsg("还没有输入地址!");
    }
}

