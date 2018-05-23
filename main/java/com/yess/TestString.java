package com.yess;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;


import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by yehun on 2018/4/17.
 */

public class TestString {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args){

        String content = "city:上海~data:2018-5-23 00:00:00!condtions[{微粒贷:3000,信用记录:信用良好,无逾期},{事业单位:true,信用记录:1年内逾期少于3次或90天}]";

        try{
            byte[] codeArray = content.getBytes("UTF-8");
            Base64.Encoder base64En = java.util.Base64.getEncoder();
            Base64.Decoder base64De = java.util.Base64.getDecoder();

            String enStr = base64En.encodeToString(codeArray);
            System.out.println(enStr);
            String encodeTwo  = base64En.encodeToString(enStr.getBytes("GBK"));
            System.out.println(encodeTwo);
            System.out.println(new String(base64De.decode(enStr),"UTF-8"));
            System.out.println(new String(base64De.decode(encodeTwo),"GBK"));

            System.out.println(new String(base64De.decode(new String(base64De.decode(encodeTwo),"GBK")),"UTF-8"));
        } catch ( Exception e){}

    }
}
