package com.jxlg.app.store.controller.manage;

import com.google.common.collect.Maps;
import com.jxlg.app.store.common.Const;
import com.jxlg.app.store.common.ResponseCode;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.entity.Product;
import com.jxlg.app.store.entity.User;
import com.jxlg.app.store.service.IFileService;
import com.jxlg.app.store.service.IProductService;
import com.jxlg.app.store.service.IUserService;
import com.jxlg.app.store.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author zhouboxi
 * @create 2017-12-04 9:10
 **/
@Controller
@RequestMapping(value = "/manage/product/")
public class ProductController {

    @Autowired
    private IProductService productService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IFileService iFileService;

    /**
     * 对产品的添加
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "add",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse save(HttpSession session, Product product){
        //判断是否是管理员,是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg("您还未登录!");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return  productService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByERRORMsg("无权访问");
    }

    /**
     * 对产品的删除
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "delete",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse delete(HttpSession session,Integer productId,Integer status){
        //判断是否是管理员,是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg("您还未登录!");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return  productService.setSaleStatus(productId,status);
        }
        return ServerResponse.createByERRORMsg("无权访问");
    }

    /**
     * 获取详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "detail",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse detail(HttpSession session,Integer productId){
        //判断是否是管理员,是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return  productService.manageProductDetail(productId);
        }
        return ServerResponse.createByERRORMsg("无权访问");
    }

    /**
     * 获取所有的商品 只要部分字段写成vo
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getAll",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getAll(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                 @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        //判断是否是管理员,是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return  productService.getProductList(pageNum,pageSize);
        }
        return ServerResponse.createByERRORMsg("无权访问");
    }

    /**
     * 筛选查询
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse search(HttpSession session,String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                 @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        //判断是否是管理员,是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
            if(user==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
            if(userService.checkAdminRole(user).isSuccess()){
            return  productService.searchProduct(productName,productId,pageNum,pageSize);
        }
            return ServerResponse.createByERRORMsg("无权访问");

    }


    /**
     * 前端的商品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "detailVo",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse detailVo(HttpSession session,Integer productId){
        //判断是否是管理员,是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return  productService.getProductDetail(productId);
        }
        return ServerResponse.createByERRORMsg("无权访问");
    }

    //对图片上传
    @RequestMapping(value = "upload",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "file",required = false)MultipartFile file){
        //判断是否是管理员,是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            String realPath = session.getServletContext().getRealPath("/upload");
            String targetFileName = iFileService.upload(file, realPath);
            String url= PropertiesUtil.getProperty("ftp.server.http.prefix")+ targetFileName;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccess(fileMap);
        }
        return ServerResponse.createByERRORMsg("无权访问");
    }

    @RequestMapping(value = "searchVo",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse searchVo(HttpSession session,String keyword,Integer categoryId, @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                   @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,String orderBy){
        //判断是否是管理员,是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return  productService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
        }
        return ServerResponse.createByERRORMsg("无权访问");


    }


}

