package com.jxlg.app.store.controller.manage;

import com.jxlg.app.store.common.Const;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.entity.User;
import com.jxlg.app.store.service.ICategoryService;
import com.jxlg.app.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author zhouboxi
 * @create 2017-12-02 17:10
 **/
@Controller
@RequestMapping(value = "/manage/category/")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "add",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        //对分类添加如果不传parentId默认为父节点为0
        //判断是否登录,
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg("你还没有登录!");
        }
        //判断是否是管理员 是返回true
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.add(parentId, categoryName);

        }else{
            return ServerResponse.createByERRORMsg("无权限操作,需要管理员权限");
        }

    }

    @RequestMapping(value = "update",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse update(HttpSession session,Integer categoryId,String categoryName){
        //判断是否登录,
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg("你还没有登录!");
        }
        //判断是否是管理员 是返回true
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.update(categoryId,categoryName);

        }else{
            return ServerResponse.createByERRORMsg("无权限操作,需要管理员权限");
        }

    }
    @RequestMapping(value = "getChildren",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0")Integer categoryId){
        //对分类添加如果不传parentId默认为父节点为0
        //判断是否登录,
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg("你还没有登录!");
        }
        //判断是否是管理员 是返回true
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.getChildrenParallelCategory(categoryId);

        }else{
            return ServerResponse.createByERRORMsg("无权限操作,需要管理员权限");
        }

    }

    @RequestMapping(value = "getAll",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        //对分类添加如果不传parentId默认为父节点为0
        //判断是否登录,
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return  ServerResponse.createByERRORMsg("你还没有登录!");
        }
        //判断是否是管理员 是返回true
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.selectCategoryAndChildrenById(categoryId);

        }else{
            return ServerResponse.createByERRORMsg("无权限操作,需要管理员权限");
        }

    }

}

