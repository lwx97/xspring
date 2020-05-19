package com.maowudi.demo.xspring.demo.mvc.action;

import com.maowudi.demo.xspring.demo.mvc.service.DemoService;
import com.maowudi.demo.xspring.mvcframework.annotation.XAutowired;
import com.maowudi.demo.xspring.mvcframework.annotation.XController;
import com.maowudi.demo.xspring.mvcframework.annotation.XRequestMapping;
import com.maowudi.demo.xspring.mvcframework.annotation.XRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@XController
@XRequestMapping("/demo")
public class DemoController {

    @XAutowired
    private DemoService demoService;

    @XRequestMapping("/test.json")
    public void test(HttpServletRequest req, HttpServletResponse resp, @XRequestParam("name") String name){
        System.out.println("-------->进入Controller");
        String result = demoService.get(name);
        System.out.println("---------返回参数："+result);
        try {
            resp.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
