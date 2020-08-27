package com.maowudi.lwxchat.controller.qrcode;

import com.alibaba.dubbo.config.annotation.Reference;
import com.maowudi.chatapi.service.QRCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 二维码服务
 */
@Controller
@RequestMapping("/qrcode")
public class QRCodeController {

    private Logger log = LoggerFactory.getLogger(QRCodeController.class);

    @Reference(interfaceClass = QRCodeService.class, timeout = 100000)
    private QRCodeService qrCodeService;

    /**
     * 测试二维码生成
     */
    @RequestMapping("/testQRCode")
    @ResponseBody
    public String testQRCode(String note){
        String url = "http://www.baidu.com";
        byte[] bytes = null;
        if(!StringUtils.hasText(note)) {
            //简单二维码
            bytes = qrCodeService.urlToQRCodeIO(url);
        }else {
            bytes = qrCodeService.urlToLogoQRCodeIO(url,new File("D:\\HD\\qrcode\\logo.jpg"),note);
        }
        File qrFile = new File("D:\\HD\\qrcode\\02.png");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(qrFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
