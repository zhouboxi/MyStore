package com.jxlg.app.store.controller.portal;

import com.jxlg.app.store.common.Const;
import com.jxlg.app.store.common.ResponseCode;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.entity.User;
import com.jxlg.app.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author zhouboxi
 * @create 2017-11-27 18:52
 **/
@Controller
@RequestMapping(value = "/user/")
public class UserController {
    @Autowired
    private IUserService iUserService;

    //用户登录
    @RequestMapping(value = "login",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String name, String password, HttpSession session){
        ServerResponse<User> response=iUserService.login(name,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    //用户退出
    @RequestMapping(value = "logout",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.invalidate();
        return  ServerResponse.createBySuccess();
    }

    //用户注册
    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    //用户检测
    @RequestMapping(value = "check",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> check(String str,String type){
        return iUserService.checkValid(str,type);
    }

    //查询用户信息
    @RequestMapping(value = "getUser",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            return  ServerResponse.createBySuccess(user);
        }
        return  ServerResponse.createByERRORMsg("你还未登录!");
    }


    //忘记密码得到问题
    @RequestMapping(value = "getQuest",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuest(String username){
        return  iUserService.selectQuestion(username);
    }

    //验证答案
    @RequestMapping(value = "checkAnswer",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    //重置密码
    @RequestMapping(value = "resetPassword",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }


    //主页重置密码有用户信息
    @RequestMapping(value = "reset_password",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByERRORMsg("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }

    //对用户信息的更新
    @RequestMapping(value = "update",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update(HttpSession session,User user){
        User users = (User) session.getAttribute(Const.CURRENT_USER);
        if(users==null){
            return  ServerResponse.createByERRORMsg("你没有登录!");
        }
        //更新吧id和用户名放到新的对象里面
        user.setId(users.getId());
        user.setUsername(users.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){
           // response.getData().setUsername(users.getUsername());
            System.out.println(response.getData().toString());
            //吧新的对象放到session中去
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;

    }

    //得到用户信息
    @RequestMapping(value = "getInformation",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByERRORMsg(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }





}

