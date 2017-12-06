package com.jxlg.app.store.common;


import com.google.common.collect.Sets;
import org.aspectj.weaver.ast.Var;

import java.util.Set;

/**
 * 这个类来存放一些验证字段
 * @author zhouboxi
 * @create 2017-11-27 20:03
 **/
public class Const {
    public static final String CURRENT_USER="currentUser";

    public static final String Email="email";
    public static final String USERNAME="username";

    public  interface  Role{
        //普通用户

        int ROLE_CUSTOMER=0;
        //管理员

        int ROLE_ADMIN=1;
    }
    public  interface checkCart{
        //选择是1

        int  CART_CHECK=1;
        //没选择是0

        int  CART_UNCHECK=0;
    }
    public  interface LimitQuantity{
        //选择是1

        String  LimitQuantity_SUCCESS="LimitQuantity_SUCCESS";
        //没选择是0

        String  LimitQuantity_FAIL="LimitQuantity_FAIL";
    }

    public enum ProductStatusEnum{
        ON_SALE(1,"在线");
        private String value;
        private int code;
        ProductStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }



        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC =Sets.newHashSet("price_desc","price_asc");

    }
    public interface  AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }


    public enum PayPlatformEnum{
        ALIPAY(1,"支付宝");
        private int code;
        private String value;

        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

    }

    public enum PaymentTypeEnum{
        ONLINE_PAY(1,"在线支付");
        private int code;
        private String value;

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static PaymentTypeEnum getDesc(int code){
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if(paymentTypeEnum.code==code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("么有找到对应的枚举");
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");
        private int code;
        private String value;

        public static OrderStatusEnum getDesc(int code){
            for (OrderStatusEnum orderStatusEnum : values()) {
                if(orderStatusEnum.code==code){
                    return orderStatusEnum;
                }
                
            }
            throw new RuntimeException("么有找到对应的枚举");
        }


        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }


}

