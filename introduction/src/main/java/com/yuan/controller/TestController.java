package com.yuan.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 一些spring boot的基本使用技巧
 * 1)在配置项中使用其他配置项
 * 2)部署时动态切换配置文件：java -jar -Dspring.profiles.active=prod XXX.jar
 */
@RestController
public class TestController {

    @Value("${description}")
    private String description;

    //可传数组，在数组中的url都可链接到该方法
    @GetMapping({"/hello", "hi"})
    public String test() {
        return "测试在配置项中使用其他配置项：" + description;
    }

    //正常情况下获取入参（http://XXXXXX?param=xxx）
    @GetMapping("/getParam")
    public String getParam(@RequestParam("id") Integer id) {
        return "id:" + id;
    }

    //在URL中获取入参
    @GetMapping("/getParamInURL/{id}")
    public String getParamInURL(@PathVariable Integer id) {
        return "id:" + id;
    }

}
