package com.yuan.service.impl;

import com.yuan.dao.UserInfoMapper;
import com.yuan.dao.UserPwdMapper;
import com.yuan.entity.UserInfo;
import com.yuan.entity.UserPwd;
import com.yuan.error.MyEmError;
import com.yuan.error.MyException;
import com.yuan.service.UserService;
import com.yuan.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserPwdMapper userPwdMapper;

    //通过用户id获取用户
    @Override
    public UserModel getUserById(Integer id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        if (userInfo == null) {
            return null;
        }
        //通过用户id获取用户的加密密码信息
        UserPwd userPwd = userPwdMapper.selectByUserId(userInfo.getId());
        return changeFromUserEntity(userInfo, userPwd);
    }

    //将UserInfo，UserPwd中的属性值，封装到UserModel中
    private UserModel changeFromUserEntity(UserInfo userInfo, UserPwd userPwd) {
        if (userInfo == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userInfo, userModel);//将UserInfo中的属性值，copy到userModel的相同属性里
        if (userPwd != null) {
            userModel.setEncrptPwd(userPwd.getEncrptPwd());
        }
        return userModel;
    }

    //用户注册功能
    @Override
    @Transactional
    public void register(UserModel userModel) throws MyException {
        if (userModel == null) {
            throw new MyException(MyEmError.PARAMETER_VALIDATION_ERROR);
        }
        if (StringUtils.isEmpty(userModel.getName()) || userModel.getGender() == null
                || userModel.getAge() == null || StringUtils.isEmpty(userModel.getTel())) {
            throw new MyException(MyEmError.PARAMETER_VALIDATION_ERROR);
        }
        UserInfo userInfo = changeFromModel(userModel);
        try {
            userInfoMapper.insertSelective(userInfo);
        } catch (DuplicateKeyException e) {
            throw new MyException(MyEmError.PARAMETER_VALIDATION_ERROR, "手机号已存在");
        }
        //如果UserInfo表的id设置为自增的话，
        //要在UserInfoMapper.xml对应的新增方法中增加keyProperty="id" useGeneratedKeys="true"，
        //才可以获得对应的自增id的值，以便进行下一步操作
        userModel.setId(userInfo.getId());
        UserPwd userPwd = changeFromModelByPwd(userModel);
        userPwdMapper.insertSelective(userPwd);
        return;
    }

    //将UserMode中的部分属性值，封装到UserInfo中
    public UserInfo changeFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userModel, userInfo);//将userModel中的属性值，copy到userInfo的相同属性里
        return userInfo;
    }

    //将UserMode中的部分属性值，封装到UserPwd中
    public UserPwd changeFromModelByPwd(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserPwd userPwd = new UserPwd();
        userPwd.setEncrptPwd(userModel.getEncrptPwd());
        userPwd.setUserId(userModel.getId());
        return userPwd;
    }

    @Override
    public UserModel validateLogin(String tel, String encrptPwd) throws MyException {
        //1，通过传入手机号获取用户信息
        UserInfo userInfo = userInfoMapper.selectByTel(tel);
        if (userInfo == null) {
            throw new MyException(MyEmError.USER_LOGIN_FAIL);
        }
        UserPwd userPwd = userPwdMapper.selectByUserId(userInfo.getId());
        UserModel userModel = changeFromUserEntity(userInfo, userPwd);
        //2，校验传入密码与数据库中密码是否相匹配
        if (!StringUtils.equals(encrptPwd, userModel.getEncrptPwd())) {
            throw new MyException(MyEmError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

}
