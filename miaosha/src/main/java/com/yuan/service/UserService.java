package com.yuan.service;

import com.yuan.error.MyException;
import com.yuan.service.model.UserModel;

public interface UserService {

    //通过用户id获取用户
    UserModel getUserById(Integer id);

    //注册功能
    void register(UserModel userModel) throws MyException;

    //校验用户登录是否合法
    UserModel validateLogin(String tel, String encrptPwd) throws MyException;

}
