package com.yuan.controller;

import com.yuan.error.MyEmError;
import com.yuan.error.MyException;
import com.yuan.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller基类
 */
public class BaseController {

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    //定义ExceptionHandler解决未被Controller层吸收的Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception e) {
        Map<String, Object> responseData = new HashMap<>();
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            responseData.put("errCode", myException.getErrCode());
            responseData.put("errMsg", myException.getErrMsg());
        } else {
            responseData.put("errCode", MyEmError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", MyEmError.UNKNOWN_ERROR.getErrMsg());
            e.printStackTrace();
        }
        return CommonReturnType.create(responseData, "fail");
    }

}
