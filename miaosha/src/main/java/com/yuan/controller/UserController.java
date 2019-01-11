package com.yuan.controller;

import com.alibaba.druid.util.StringUtils;
import com.yuan.controller.viewObj.UserVO;
import com.yuan.error.MyEmError;
import com.yuan.error.MyException;
import com.yuan.response.CommonReturnType;
import com.yuan.service.UserService;
import com.yuan.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping("toGetOtp")
    public String toGetOtp() {
        return "getotp";
    }

    //用户获取otp短信接口
    @RequestMapping(value = "/getOtp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "tel") String tel) {
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);//创建0-99999以内的随机数
        randomInt += 10000;//此时随机数范围：10000-109999
        String otpCode = String.valueOf(randomInt);
        //将OTP验证码同对应用户的手机号关联，暂时使用httpsession的方式绑定用户的手机和otpCode，实际项目中用reids
        httpServletRequest.getSession().setAttribute(tel, otpCode);
        //将OTP验证码通过短信发送给用户（此处省略，只打印）
        System.out.println("tel=" + tel + "&otpCode=" + otpCode);
        return CommonReturnType.create(null);//不需要返回data
    }

    @RequestMapping("toRegister")
    public String toRegister() {
        return "register";
    }

    //用户注册接口
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "tel") String tel,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "pwd") String pwd)
            throws MyException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //1，验证手机号和对应的otpCode相符合
        //获取之前getOtp方法中放在Session中的otpCode
        String otpCodeInSession = (String) this.httpServletRequest.getSession().getAttribute(tel);
        //与传入的otpCode做比较，看是否一致
        if (!StringUtils.equals(otpCode, otpCodeInSession)) {//不一致
            throw new MyException(MyEmError.PARAMETER_VALIDATION_ERROR, "短信验证码不一致");
        }
        //2，用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTel(tel);
        userModel.setRegisterModel("byPhone");
        userModel.setEncrptPwd(this.EncodeByMD5(pwd));
        userService.register(userModel);
        return CommonReturnType.create(null);//不需要返回data
    }

    //加密方法
    public String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //获取加密字符串
        String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    @RequestMapping("toLogin")
    public String toLogin() {
        return "login";
    }

    //用户登录接口
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "tel") String tel,
                                  @RequestParam(name = "pwd") String pwd)
            throws MyException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //1，入参校验
        if (org.apache.commons.lang3.StringUtils.isEmpty(tel) || StringUtils.isEmpty(pwd)) {
            throw new MyException(MyEmError.PARAMETER_VALIDATION_ERROR);
        }
        //2，校验用户登录是否合法
        UserModel userModel = userService.validateLogin(tel, this.EncodeByMD5(pwd));
        //3，如果第二步没有抛出任何异常，将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
        return CommonReturnType.create(null);//不需要返回data
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws MyException {
        UserModel userModel = userService.getUserById(id);
        if (userModel == null) {
            throw new MyException(MyEmError.USER_NOT_EXIST);
        }
        UserVO userVO = changeFromUserModel(userModel);
        return CommonReturnType.create(userVO);
    }

    //将UserModel中的属性值，封装到UserVO中相同的属性中去
    private UserVO changeFromUserModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);//将userModel中的属性值，copy到userVO的相同属性里
        return userVO;
    }

}
