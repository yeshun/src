package com.yess;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by yehun on 2018/4/17.
 */
public class TestString {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args){

    String content = "南京M" +
            "2018-6-30Space00:00:00N" +
            "23L53O" +
            "无#" +
            "无A" +
            "连续6个月B" +
            "无C" +
            "无D" +
            "无E" +
            "无F" +
            "无G" +
            "1年内逾期少于3次且少于90天;信用良,无逾期H" +
            "无I" +
            "无J" +
            "无K" +
            "无" +
            "||" +
            "无#" +
            "无A" +
            "无B" +
            "连续6个月C" +
            "无D" +
            "无E" +
            "无F" +
            "无G" +
            "1年内逾期少于3次且少于90天;信用良,无逾期H" +
            "无I" +
            "无J" +
            "无K" +
            "无" +
            "||" +
            "无#" +
            "无A" +
            "无B" +
            "无C" +
            "无D" +
            "无E" +
            "无F" +
            "无G" +
            "1年内逾期少于3次且少于90天;信用良,无逾期H" +
            "10000I" +
            "无J" +
            "无K" +
            "无" ;
        try{
          /* content = content.trim();
            byte[] codeArray = content.getBytes("UTF-8");
            String enStr = new String(Base64.encode(codeArray, Base64.NO_WRAP)) ;
            System.out.println(enStr);
            if(false){
                List<TestSmali.OrdreFilter> list = TestSmali.getInstance().ParseYessKey(enStr);
                for (TestSmali.OrdreFilter order:list) {
                    System.out.println("城市 : " +order.cityFlag);
                    System.out.println("到期时间 : " +order.lockFlag);
                    System.out.println("年龄范围 : " +order.minAge +"-"+order.maxAge);
                    System.out.println("月收入 : " +order.月收入);
                    System.out.println("收入形式 : " +order.收入形式);
                    System.out.println("本地社保 : " +order.本地社保);
                    System.out.println("本地公积金 : " +order.本地公积金);
                    System.out.println("当前单位工龄 : " +order.当前单位工龄);
                    System.out.println("手机归属地 : " +order.手机归属地);
                    System.out.println("户籍所在地 : " +order.户籍所在地);
                    System.out.println("信用卡额度 : " +order.信用卡额度);
                    System.out.println("信用记录 : " +order.信用记录);
                    System.out.println("微粒贷额度 : " +order.微粒贷额度);
                    System.out.println("名下房产 : " +order.名下房产);
                    System.out.println("名下车产 : " +order.名下车产);
                    System.out.println("保单价值 : " +order.保单价值);
                    System.out.println("==================================================================");
                }
            }*/

            List<Boolean> allCondition = new ArrayList<Boolean>();
            allCondition.add(false);
            System.out.println(allCondition.contains(false));

        /*    TestSmali.OrdreFilter filter = new TestSmali.OrdreFilter();
            filter.微粒贷额度 = "无";
            filter.微粒贷额度 = "无";
            filter.收入形式 = "无";
            System.out.println(filter.ValiCount());*/
        } catch ( Exception e){
            e.printStackTrace();
        }


        String data = "2018-6-30Space00:00:00";
      //  String ANDROID_ID = Settings.System.getString(TestSmali.IndexActivity.getContentResolver(), Settings.System.ANDROID_ID);
        String sKey = "yeshun_296457808";
        System.out.print(Encrypt(data,sKey));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String Encrypt(String sSrc, String sKey) {
        try{
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));


            return Base64.getEncoder().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
