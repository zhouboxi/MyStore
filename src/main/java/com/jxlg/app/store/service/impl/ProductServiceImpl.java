package com.jxlg.app.store.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.jxlg.app.store.common.Const;
import com.jxlg.app.store.common.ResponseCode;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.dao.CategoryMapper;
import com.jxlg.app.store.dao.ProductMapper;
import com.jxlg.app.store.entity.Category;
import com.jxlg.app.store.entity.Product;
import com.jxlg.app.store.service.ICategoryService;
import com.jxlg.app.store.service.IProductService;
import com.jxlg.app.store.vo.ProductDetailVo;
import com.jxlg.app.store.vo.ProductListVo;
import com.jxlg.app.store.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouboxi
 * @create 2017-12-02 20:23
 **/
@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        //对商品进行保存和更新
        if(product!=null){
            //把所有图片的第一张图为展示图
            if(StringUtils.isNotBlank(product.getSunImages())){
                String[] split  = product.getSunImages().split(",");
                if(split.length>0){
                    product.setMainImage(split[0]);
                }
            }

            if(product.getId()!=null){
                //说明是更新操作
                int i = productMapper.updateByPrimaryKeySelective(product);
                if(i>0){
                    return  ServerResponse.createBySuccess("更新成功!");
                }
                return  ServerResponse.createByERRORMsg("更新失败");
            }else{
                int insert = productMapper.insert(product);
                if(insert>0){
                    return  ServerResponse.createBySuccess("保存成功!");
                }
                return  ServerResponse.createByERRORMsg("保存失败");
            }
        }
        return ServerResponse.createByERRORMsg("参数异常");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        //对商品的删除
        if(productId == null || status == null){
            return ServerResponse.createByERRORMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int i = productMapper.updateByPrimaryKeySelective(product);
        if(i>0){
            return  ServerResponse.createBySuccess("删除成功!");
        }
        return ServerResponse.createByERRORMsg("删除失败!");
    }

    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        //通过id查询详情
        if(productId==null){
            return ServerResponse.createByERRORMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createByERRORMsg("产品已下架!");
        }
        ProductDetailVo productDetailVo = changeToVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    //设置VO面向前端

    private ProductDetailVo changeToVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSunImages(product.getSunImages());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setCreateTime(product.getCreateTime());
        productDetailVo.setUpdateTime(product.getUpdateTime());
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://图片服务器/"));
        Category category = categoryMapper.selectByPrimaryKey(productDetailVo.getCategoryId());
        if(category==null){
            //默认根节点
            productDetailVo.setParentCategoryId(0);
        }
        productDetailVo.setParentCategoryId(category.getParentId());

        return productDetailVo;

    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        //startPage--start
        //填充自己的sql查询逻辑
        //pageHelper-收尾
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.findAll();
        List<ProductListVo> list = Lists.newArrayList();
        for (Product product : products) {
            ProductListVo productListVo = assembleProductListVo(product);
            list.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(list);
        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName=new StringBuffer().append("%").append(productName).append("%").toString();
        }
        List<Product> products = productMapper.findByNameAndId(productName, productId);
        List<ProductListVo> list = Lists.newArrayList();
        for (Product product : products) {
            list.add(assembleProductListVo(product));
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(list);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if(productId == null){
            return ServerResponse.createByERRORMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createByERRORMsg("产品已经下架");
        }
        if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByERRORMsg("产品已经下架或删除");
        }
        ProductDetailVo productDetailVo = changeToVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    @Override
    public ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        //前端搜索显示内容
        if(StringUtils.isBlank(keyword)&&categoryId==null){
            return ServerResponse.createByERRORMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList();
        if(categoryId!=null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category==null&&StringUtils.isBlank(keyword)){
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword=new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        //排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] split = orderBy.split("_");
                PageHelper.orderBy(split[0]+" "+split[1]);
            }
        }
        List<Product> products = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product product : products){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}

