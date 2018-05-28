package com.yess;
import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yehun on 2018/4/17.
 */
public class TestString {

    public static void main(String[] args){

    String content = "上海M" +
            "2018-5-23Space00:00:00N" +
            "22L50O" +
            "3000#" +
            "银行转账A" +
            "连续6个月B" +
            "连续6个月C" +
            "6个月以上D" +
            "上海E" +
            "上海F" +
            "8000G" +
            "1年内逾期少于3次且少于90天H" +
            "10000I" +
            "有房产,可接受抵押J" +
            "有车产,不接受抵押K" +
            "30万" +
            "||" +
            "无#" +
            "无A" +
            "无B" +
            "无C" +
            "无D" +
            "无E" +
            "无F" +
            "无G" +
            "无H" +
            "无I" +
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
    }
}
