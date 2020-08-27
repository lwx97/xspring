package com.maowudi.chatproducer.util.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.IOException;


/**
 * 二维码工具类
 */
public class QRCodeUtil {


    public static byte[] urlToORCodeIO(String url) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix encode = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 350, 350);
        ByteOutputStream byteOutputStream = new ByteOutputStream();
        MatrixToImageWriter.writeToStream(encode,"PNG",byteOutputStream);
        byte[] bytes = byteOutputStream.getBytes();
        byteOutputStream.close();
        return bytes;
    }



}
