package com.jxlg.app.store.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.jxlg.app.store.common.Const;
import com.jxlg.app.store.common.ResponseCode;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.entity.User;
import com.jxlg.app.store.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhouboxi
 * @create 2017-12-05 19:19
 **/
@Controller
@RequestMapping(value = "/order/")
public class OrderController {
    @Autowired
    private IOrderService orderService;
    private static final Logger logger= LoggerFactory.getLogger(OrderController.class);
    @RequestMapping("create")
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer shippingId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.create(user.getId(),shippingId);
    }


    @RequestMapping("cancel")
    @ResponseBody
    public ServerResponse cancel(HttpSession session, Long orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.cancel(user.getId(),orderNo);
    }


    @RequestMapping("getOrderCartProduct")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderProduct(user.getId());
    }



    @RequestMapping("detail")
    @ResponseBody
    public ServerResponse detail(HttpSession session,Long orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderDetail(user.getId(),orderNo);
    }

    @RequestMapping("list")
    @ResponseBody
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderList(user.getId(),pageNum,pageSize);
    }
























    @RequestMapping("pay")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = session.getServletContext().getRealPath("/upload");
        return orderService.pay(orderNo,user.getId(),path);
    }

    @RequestMapping("callback")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){

        Map<String,String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
            String name = (String)iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for(int i = 0 ; i <values.length;i++){

                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
            }
            params.put(name,valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.

        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());

            if(!alipayRSACheckedV2){
                return ServerResponse.createByERRORMsg("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
        }

        //todo 验证各种数据

        //
        ServerResponse serverResponse = orderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }


    @RequestMapping("queryOrderPayStatus")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse serverResponse = orderService.queryOrderPayStatus(user.getId(),orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }





}

