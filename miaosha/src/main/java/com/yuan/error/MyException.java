package com.yuan.error;

/**
 * 系统统一业务异常的处理类
 */
public class MyException extends Exception implements CommonError {

    private CommonError commonError;

    //直接接收MyEmError的传参，用于构造业务异常
    public MyException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    //接收自定义errMsg构造业务异常
    public MyException(CommonError commonError, String errMsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }

}
