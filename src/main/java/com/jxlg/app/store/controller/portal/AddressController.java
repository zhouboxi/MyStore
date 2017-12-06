package com.jxlg.app.store.controller.portal;

import com.jxlg.app.store.common.Const;
import com.jxlg.app.store.common.ResponseCode;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.entity.Shipping;
import com.jxlg.app.store.entity.User;
import com.jxlg.app.store.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author zhouboxi
 * @create 2017-12-05 11:26
 **/
@RequestMapping(value = "/address/")
@Controller
public class AddressController {

    @Autowired
    private IAddressService addressService;
    //对地址添加
    @RequestMapping(value = "add")
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping){
      User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return addressService.add(user.getId(),shipping);
    }

    //对地址的修改
    @RequestMapping(value = "update")
    @ResponseBody
    public ServerResponse update(HttpSession session, Shipping shipping){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return addressService.update(user.getId(),shipping);
    }

    //对地址的删除
    @RequestMapping(value = "delete")
    @ResponseBody
    public ServerResponse delete(HttpSession session, Integer addressId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return addressService.delete(user.getId(),addressId);
    }
    //对地址的查询
    //对地址的修改
    @RequestMapping(value = "findOne")
    @ResponseBody
    public ServerResponse findOne(HttpSession session, Integer addressId){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return addressService.findOne(user.getId(),addressId);
    }

    //对所有的地址就行查询
    @RequestMapping(value = "list")
    @ResponseBody
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){
        User user= (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return addressService.list(user.getId(),pageNum,pageSize);
    }
}

