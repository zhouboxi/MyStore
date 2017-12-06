package com.jxlg.app.store.service.impl;

import com.jxlg.app.store.common.Const;
import com.jxlg.app.store.common.ServerResponse;
import com.jxlg.app.store.common.TokenCache;
import com.jxlg.app.store.dao.UserMapper;
import com.jxlg.app.store.entity.User;
import com.jxlg.app.store.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author zhouboxi
 * @create 2017-11-27 18:55
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    //登录
   @Override
    public ServerResponse<User> login(String username, String password) {
       int resultCount = userMapper.checkUserName(username);
       if (resultCount == 0) {
           return  ServerResponse.createByERRORMsg("用户名不存在!");
       }
       //todo 密码登录MD5
        User user=userMapper.selectLogin(username,password);
       if(user==null){
           return ServerResponse.createByERRORMsg("密码错误!");
       }
       user.setPassword(StringUtils.EMPTY);
       return  ServerResponse.createBySuccess("登录成功",user);
    }

    //注册
    @Override
    public ServerResponse<String> register(User user) {
        //在注册之前先调用对用户名和邮箱的验证
        ServerResponse response = this.checkValid(user.getUsername(), Const.USERNAME);
        if(!response.isSuccess()){
            return  response;
        }
        //对密码验证
        response=this.checkValid(user.getEmail(),Const.Email);
        if(!response.isSuccess()){
            return  response;
        }
        //如果都通过就行授权
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //todo 对密码加密
        int insert = userMapper.insert(user);
        if(insert==0){
            return ServerResponse.createByERRORMsg("注册失败!");
        }
        return ServerResponse.createBySuccessMsg("注册成功!");
    }


    //注册的时候检测用户名或者邮箱是否被使用
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
       //判断是否为空
       if(StringUtils.isNotBlank(type)){
           //当鼠标从form表单的username移开触发
           if(Const.USERNAME.equals(type)){
               int resultCount = userMapper.checkUserName(str);
               if(resultCount>0){
                   return ServerResponse.createByERRORMsg("用户名已存在!");
               }
           }
           if(Const.Email.equals(type)){
               int resultCont=userMapper.checkEmail(str);
               if(resultCont>0){
                   return  ServerResponse.createByERRORMsg("邮箱已被使用");
               }
           }

       }else{
           return  ServerResponse.createBySuccess("参数错误!");
       }
        return ServerResponse.createBySuccessMsg("校验成功!");
    }


    //用户忘记密码对密码找回通过选择问题
    //首先用户必须是存在
    @Override
    public ServerResponse selectQuestion(String username) {
        ServerResponse<String> response = this.checkValid(username, Const.USERNAME);
        //如果查询不到返回错误判断是否为成功
        if(response.isSuccess()){
            return  ServerResponse.createByERRORMsg("用户不存在!");
        }
        String question=userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByERRORMsg("你没有设置问题!");
    }

    //对问题的回答验证
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int checkAnswer = userMapper.checkAnswer(username, question, answer);
        if(checkAnswer>0){
            //问题回答正确
            String forgetToken = UUID.randomUUID().toString();
            //生成一个唯一的12小时验证id,也就是说12小时内重置密码有效,防止被别人恶意修改
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return  ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByERRORMsg("问题回答错误!");
    }


    //忘记密码 通过回答问题重置密码
    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if(StringUtils.isBlank(forgetToken)){
            //如果
            return ServerResponse.createByERRORMsg("参数错误!");
        }
        //检测是否存在用户
        ServerResponse<String> response = this.checkValid(username, Const.USERNAME);
        if(response.isSuccess()){
            return  ServerResponse.createByERRORMsg("用户不存在!");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByERRORMsg("token无效或者过期");
        }
       if(StringUtils.equals(token,forgetToken)){
            //TODO 密码加密
           int resultCount = userMapper.updatePasswordByUsername(username, passwordNew);
           if(resultCount>0) {
               return ServerResponse.createBySuccess("密码修改成功");
           }
       }else{
           return  ServerResponse.createByERRORMsg("token错误请重新获取!");
       }
        return ServerResponse.createByERRORMsg("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
    //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.
    // 因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
        int resultCount = userMapper.checkPassword(passwordOld, user.getId());
        if(resultCount==0){
            return  ServerResponse.createByERRORMsg("旧密码错误");
        }
        //吧新密码放到对象中去
        //TODO 密码加密
        user.setPassword(passwordNew);
        int i = userMapper.updateByPrimaryKey(user);
        if(i>0){
            return  ServerResponse.createBySuccess("密码更新成功!");
        }
        return ServerResponse.createByERRORMsg("密码更新失败!");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在,
        // 并且存在的email如果相同的话,不能是我们当前的这个用户的.

        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServerResponse.createByERRORMsg("email已存在,请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByERRORMsg("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByERRORMsg("找不到当前用户");
        }
        //通过id查找吧id值空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
       //判断是否为管理员
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByERROR();
    }
}

