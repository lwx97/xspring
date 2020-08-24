package com.maowudi.chatproducer;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableDubboConfiguration
public class ChatproducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatproducerApplication.class, args);
    }

}
