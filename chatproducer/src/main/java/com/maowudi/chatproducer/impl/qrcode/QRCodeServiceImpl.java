package com.maowudi.chatproducer.impl.qrcode;


import com.alibaba.dubbo.config.annotation.Service;
import com.google.zxing.WriterException;
import com.maowudi.chatapi.service.QRCodeService;
import com.maowudi.chatproducer.util.qrcode.QRCodeUtil;
import com.maowudi.chatproducer.util.qrcode.ZXingCode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Service(interfaceClass = QRCodeService.class , timeout = 60000)
@Component
public class QRCodeServiceImpl implements QRCodeService {

    @Override
    public byte[] urlToQRCodeIO(String url) {
        byte[] bytes = new byte[0];
        try {
            bytes = QRCodeUtil.urlToORCodeIO(url);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public byte[] urlToLogoQRCodeIO(String qrUrl, File logoFile, String note) {
        byte[] bytes = ZXingCode.drawLogoQRCode(logoFile, qrUrl, note);
        return bytes;
    }
}
