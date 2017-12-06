package com.jxlg.app.store.service;

import com.github.pagehelper.PageInfo;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.vo.OrderVo;

import java.util.Map;

/**
 * @author zhouboxi
 * @create 2017-12-05 19:18
 **/
public interface IOrderService {
    ServerResponse pay(Long orderNo, Integer userId, String path);
    ServerResponse aliCallback(Map<String,String> params);
    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);

    ServerResponse<String> cancel(Integer userId,Long orderNo);
    ServerResponse create(Integer userId,Integer shippingId);
    ServerResponse getOrderProduct(Integer userId);
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);


    ServerResponse<PageInfo> manageList(int pageNum,int pageSize);
    ServerResponse<OrderVo> manageDetail(Long orderNo);
    ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);
    ServerResponse<String> manageSendGoods(Long orderNo);
}
