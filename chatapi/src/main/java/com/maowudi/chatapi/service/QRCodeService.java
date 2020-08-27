package com.maowudi.chatapi.service;


import java.io.File;

/**
 * 二维码服务
 */
public interface QRCodeService {


    /**
     * 将url网址转化为二维码 ，以流输出
     * @param url
     * @return
     */
    byte[] urlToQRCodeIO(String url);

    /**
     * 带LOGO或描述的二维码
     * @param qrUrl
     * @param logoFile
     * @param note
     * @return
     */
    byte[] urlToLogoQRCodeIO(String qrUrl, File logoFile,String note);

}
