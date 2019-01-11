package com.yuan.dao;

import com.yuan.entity.UserPwd;

public interface UserPwdMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPwd record);

    int insertSelective(UserPwd record);

    UserPwd selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPwd record);

    int updateByPrimaryKey(UserPwd record);

    UserPwd selectByUserId(Integer userId);
}