package com.yuan.springboot_hotdeploy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wangy on 2019/1/3.
 */
@Controller
public class HotDeployController {

    @RequestMapping(value = "/say", method = RequestMethod.GET)
    public String say(HttpServletRequest request) {
        request.setAttribute("say", "wang yuan");
        return "index";
    }

}
