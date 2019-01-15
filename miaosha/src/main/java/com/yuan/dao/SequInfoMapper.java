package com.yuan.dao;

import com.yuan.entity.SequInfo;

public interface SequInfoMapper {
    int deleteByPrimaryKey(String name);

    int insert(SequInfo record);

    int insertSelective(SequInfo record);

    SequInfo selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(SequInfo record);

    int updateByPrimaryKey(SequInfo record);

    //锁表获取自增值
    SequInfo getSequenceByName(String name);
}