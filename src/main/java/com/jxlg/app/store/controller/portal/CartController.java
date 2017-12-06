package com.jxlg.app.store.controller.portal;

import com.jxlg.app.store.common.Const;
import com.jxlg.app.store.common.ResponseCode;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.entity.User;
import com.jxlg.app.store.service.ICartService;
import com.jxlg.app.store.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 购物车模块
 * @author zhouboxi
 * @create 2017-12-04 19:54
 **/
@Controller
@RequestMapping(value = "/cart/")
public class CartController {
    @Autowired
    private ICartService cartService;

    //添加购物车
    @RequestMapping(value = "add")
    @ResponseBody
    public ServerResponse add(HttpSession session,Integer productId,Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg("请登录!");
        }
        return  cartService.add(user.getId(),productId,count);
    }

    //对购物车的更新
    @RequestMapping(value = "update")
    @ResponseBody
    public ServerResponse update(HttpSession session,Integer productId,Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.update(user.getId(), productId, count);
    }

    //对购物车的删除
    @RequestMapping(value = "delete")
    @ResponseBody
    public ServerResponse delete(HttpSession session,String productIds){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.delete(user.getId(), productIds);
    }

    //对购物车的查看
    @RequestMapping(value = "list")
    @ResponseBody
    public ServerResponse list(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.list(user.getId());
    }

    //全选
    @RequestMapping(value = "selectAll")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session){
       User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(),null,Const.checkCart.CART_CHECK);
    }

    //全反选
    @RequestMapping(value = "unSelectAll")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(),null,Const.checkCart.CART_UNCHECK);
    }

    //单独选
    @RequestMapping(value = "selectOne")
    @ResponseBody
    public ServerResponse<CartVo> selectOne(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(),productId,Const.checkCart.CART_CHECK);
    }
    //单独反选
    @RequestMapping(value = "unSelectOne")
    @ResponseBody
    public ServerResponse<CartVo> unSelectOne(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(),productId,Const.checkCart.CART_UNCHECK);
    }



    //查询当前用户的购物车里面的产品数量,如果一个产品有10个,那么数量就是10.
    @RequestMapping("getCartProductCount")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createBySuccess(0);
        }
        return cartService.getCartProductCount(user.getId());
    }
}

