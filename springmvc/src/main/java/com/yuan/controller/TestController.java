package com.yuan.controller;

import com.yuan.entity.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用于演示springmvc的数据绑定
 */
@Controller
public class TestController {

    /**
     * 由以下两个请求可以看出，在两个请求都不配置required = false的情况下。
     * 入参为基本类型时，age参数在请求中必填。而入参为包装类型时，age参数可为空
     */
    @RequestMapping("base")
    @ResponseBody
    public String base(int age) {
        return "age===" + age;
    }

    @RequestMapping("base2")
    @ResponseBody
    public String base2(Integer age) {
        return "age===" + age;
    }

    /**
     * springmvc绑定数组类型
     * 请求参数：http://localhost:8080/array?name=aaa&name=bbb&name=ccc
     */
    @RequestMapping("array")
    @ResponseBody
    public String array(String[] name) {
        StringBuilder sb = new StringBuilder();
        for (String s : name) {
            sb.append(s).append("   ");
        }
        return sb.toString();
    }

    /**
     * springmvc绑定对象类型
     * 在User对象中还有UserSon这种子属性类的情况下，直接绑定属性值如下:
     * http://localhost:8080/object?name=wyuan&age=20&son.tel=110&son.addr=jjjj
     */
    @RequestMapping("object")
    @ResponseBody
    public String object(User user) {
        return user.toString();
    }

    /**
     * springmvc绑定2个有相同属性的对象类型
     * 加上两个@InitBinder辅助方法后可以按以下url分别绑定到指定对象
     * http://localhost:8080/sameObject?user.name=wyuan&user.age=20&userSame.name=aaaa&userSame.age=11
     */
    @RequestMapping("sameObject")
    @ResponseBody
    public String sameObject(User user, UserSame userSame) {
        return user.toString() + "  " + userSame.toString();
    }

    //User对象辅助前缀
    @InitBinder("user")
    public void initUser(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user.");
    }

    //UserSame对象辅助前缀
    @InitBinder("userSame")
    public void initUserSame(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("userSame.");
    }

    /**
     * springmvc绑定List类型
     * 需要建一个像UserListForm这样的类，直接在入参中放入List<User>是无法绑定的
     * http://localhost:8080/list?users[0].name=www&users[1].name=yyy&users[2].name=ccc
     * 注意：
     * 1）高版本的Tomcat中这样的请求必须用POST方式来请求（这里就是使用POST方式），
     * 直接用GET请求是会报错的
     * 2）如果下标不按顺序，如0，1，20这样的下标，那么会直接把2-19的下标对象全部赋值null，list的size也会是21
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public String list(UserListForm form) {
        return "list size：" + form.getUsers().size() + "  " + form.toString();
    }

    /**
     * springmvc绑定Set类型
     * http://localhost:8080/list?users[0].name=www&users[1].name=yyy
     * 和组装List的区别：set的size必须初始化，size多长决定能放几个参数，如果size是4，那后面的对象就只能4个
     * 所以在集合选择上优先选择List
     */
    @RequestMapping(value = "set", method = RequestMethod.POST)
    @ResponseBody
    public String set(UserSetForm form) {
        return "set size：" + form.getUsers().size() + "  " + form.toString();
    }

    /**
     * springmvc绑定Map类型
     * http://localhost:8080/list?map['xxx'].name=wyuan&users['xxx'].age=11
     */
    @RequestMapping(value = "map", method = RequestMethod.POST)
    @ResponseBody
    public String map(UserMapForm form) {
        return form.toString();
    }

}
