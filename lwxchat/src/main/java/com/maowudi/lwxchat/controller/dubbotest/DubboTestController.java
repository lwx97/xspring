package com.maowudi.lwxchat.controller.dubbotest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.maowudi.chatapi.service.DubboTest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DubboTestController {

    @Reference(check = false, timeout = 1000000)
    private DubboTest dubboTest;

    @RequestMapping("test")
    public String dubbotest(String hello){
        return dubboTest.dubboTest(hello);
    }
}
