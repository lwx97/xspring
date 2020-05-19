package com.maowudi.demo.xspring.demo.mvc.service.impl;

import com.maowudi.demo.xspring.demo.mvc.service.DemoService;
import com.maowudi.demo.xspring.mvcframework.annotation.XService;

@XService
public class DemoServiceImpl implements DemoService {
    public String get(String name) {
        return "my test is "+name;
    }
}
