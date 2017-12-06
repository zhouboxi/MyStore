package com.jxlg.app.store.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.jxlg.app.store.common.Const;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.dao.CartMapper;
import com.jxlg.app.store.dao.ProductMapper;
import com.jxlg.app.store.entity.Cart;
import com.jxlg.app.store.entity.Product;
import com.jxlg.app.store.service.ICartService;
import com.jxlg.app.store.util.BigDecimalUtil;
import com.jxlg.app.store.util.PropertiesUtil;
import com.jxlg.app.store.vo.CartProductVo;
import com.jxlg.app.store.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouboxi
 * @create 2017-12-04 19:59
 **/
@Service
public class CartServiceImpl implements ICartService{

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer productId,Integer count) {
        if(productId==null||count==null){
            return  ServerResponse.createByERRORMsg("参数错误!");
        }
        //查询是否存在这个购物车 存在更新,不存在创建//id有但是没有对应商品会报错
        Cart cart = cartMapper.checkCart(userId, productId);
        //没有查找到需要对商品id进行对比
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return  ServerResponse.createByERRORMsg("参数错误!");
        }
        if(cart==null){
            Cart carts = new Cart();
            carts.setUserId(userId);
            carts.setProductId(productId);
            carts.setChecked(Const.checkCart.CART_CHECK);
            carts.setQuantity(count);
            cartMapper.insert(carts);
        }else{
            //这个产品已经在购物车里了.
            //如果产品已存在,数量相加
            count = cart.getQuantity()+count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);

        }
        return this.list(userId);
    }

    //对购物车的更新
    @Override
    public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){
        if(productId==null||count==null){
            return  ServerResponse.createByERRORMsg("参数错误!");
        }
        //如果购物车的商品为空
        Cart cart = cartMapper.checkCart(userId, productId);
        if(cart!=null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);
    }

    //对购物车的删除 删除多个吧id变成String;类型进行分割
    @Override
    public ServerResponse<CartVo> delete(Integer userId,String productIds){
        List<String> productId = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productId)){
            return  ServerResponse.createByERRORMsg("参数错误!");
        }
        cartMapper.delete(userId, productId);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> list (Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    //我们对购物车的任何操作都要计算得到具体信息
    public CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.findByUserId(userId);
        //用来保存转变之后的vo
        ArrayList<CartProductVo> cartProductVos = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if(CollectionUtils.isNotEmpty(cartList)){
            for (Cart cart : cartList) {
                CartProductVo productVos = new CartProductVo();
                productVos.setId(cart.getId());
                productVos.setUserId(userId);
                productVos.setProductId(cart.getProductId());
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if(product!=null){
                    productVos.setProductName(product.getName());
                    productVos.setProductMainImage(product.getMainImage());
                    productVos.setProductPrice(product.getPrice());
                    productVos.setProductStatus(product.getStatus());
                    productVos.setProductStock(product.getStock());
                    //判断添加数量是否超过库存
                    int buyLimitCount = 0;
                    if(product.getStock()>cart.getQuantity()){
                        //库存充足的时候
                        buyLimitCount= cart.getQuantity();
                        productVos.setLimitQuantity(Const.LimitQuantity.LimitQuantity_SUCCESS);
                    }else{
                        //库存不足的时候把所有都给他
                        buyLimitCount = product.getStock();
                        productVos.setLimitQuantity(Const.LimitQuantity.LimitQuantity_FAIL);
                        //购物车中更新有效库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cart.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    productVos.setQuantity(buyLimitCount);
                    productVos.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),buyLimitCount));
                    productVos.setProductChecked(cart.getChecked());
                }
                if(cart.getChecked() == Const.checkCart.CART_CHECK){
                    //如果已经勾选,增加到整个的购物车总价中
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),productVos.getProductTotalPrice().doubleValue());
                }
                cartProductVos.add(productVos);
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVos);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }

    @Override
    public ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked){
        cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
        return  this.list(userId);
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }

}

