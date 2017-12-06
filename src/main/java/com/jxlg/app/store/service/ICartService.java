package com.jxlg.app.store.service;

import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.vo.CartVo;

/**
 * @author zhouboxi
 * @create 2017-12-04 19:59
 **/
public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo>  delete(Integer userId,String productIds);

    ServerResponse<CartVo> list (Integer userId);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}
