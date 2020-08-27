package com.maowudi.chatproducer.impl.dubbotest;

import com.alibaba.dubbo.config.annotation.Service;
import com.maowudi.chatapi.service.DubboTest;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = DubboTest.class ,timeout = 600000)
public class DubboTestImpl implements DubboTest{

    @Override
    public String dubboTest(String hello) {
        return hello+" success!";
    }
}
