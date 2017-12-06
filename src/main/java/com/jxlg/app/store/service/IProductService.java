package com.jxlg.app.store.service;

import com.github.pagehelper.PageInfo;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.entity.Product;
import com.jxlg.app.store.vo.ProductDetailVo;

/**
 * @author zhouboxi
 * @create 2017-12-02 20:21
 **/
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId,Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);



}
