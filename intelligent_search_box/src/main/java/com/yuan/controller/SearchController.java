package com.yuan.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    static List<String> datas = new ArrayList<>();

    static {
        //模拟数据库中的数据
        datas.add("ajax");
        datas.add("ajax post");
        datas.add("jordan");
        datas.add("kobe");
        datas.add("james");
        datas.add("中文");
        datas.add("中文abc");
        datas.add("数字123");
    }

    @RequestMapping("toSearch")
    public String toSearch() {
        return "search";
    }

    @RequestMapping("search")
    @ResponseBody
    public String search(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        //获取前台数据==keyword
        String keyword = request.getParameter("keyword");
        //模拟去数据库查询出了结果
        List<String> listData = getData(keyword);
        //返回json格式
        Gson gson = new Gson();
        return gson.toJson(listData);
    }

    //模拟去数据库查询返回了List集合
    public List<String> getData(String keyword) {
        List<String> list = new ArrayList<>();
        for (String data : datas) {
            if (data.contains(keyword)) {
                list.add(data);
            }
        }
        return list;
    }

}
