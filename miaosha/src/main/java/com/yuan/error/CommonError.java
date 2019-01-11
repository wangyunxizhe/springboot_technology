package com.yuan.error;

/**
 * 系统统一错误码接口
 */
public interface CommonError {

    int getErrCode();

    String getErrMsg();

    CommonError setErrMsg(String errMsg);

}
