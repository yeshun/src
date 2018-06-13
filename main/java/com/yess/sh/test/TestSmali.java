package com.yess.sh.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.huijiemanager.app.ApplicationController;
import com.huijiemanager.base.b;
import com.huijiemanager.http.NetworkHelper;
import com.huijiemanager.http.response.MyInforCreditResponse;
import com.huijiemanager.http.response.PublicDetailResponse;
import com.huijiemanager.http.response.QuareOrderFiltrateResponse;
import com.huijiemanager.ui.activity.PublicDetailActivity;
import com.huijiemanager.ui.fragment.PageFragment;
import com.huijiemanager.utils.ao;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by yehun on 2018/4/14.
 */
public class TestSmali {

    public static TestSmali instance;
    private static  String TAG = "yess : ";
    public  static  void LogStr(String parmeras)
    {
        Log.d(TAG,parmeras);
    }

    public static  boolean startAgent = false;

    public static  boolean autoCheck = false;

    private static MenuItem detailClose;

    private  static  ArrayList<Integer> rededOrders = new ArrayList<Integer>();
    private static ArrayList<QuareOrderFiltrateResponse.OrdersBean> allOrder = new ArrayList<QuareOrderFiltrateResponse.OrdersBean>();
    private static HashMap<QuareOrderFiltrateResponse.OrdersBean,PageFragment> allPage = new HashMap<QuareOrderFiltrateResponse.OrdersBean,PageFragment>();
    // private static int queryCounter = 5;

    private static PageFragment lastFragment;

    //  private static ScheduledThreadPoolExecutor pool;

    private static int delayInterval = 600;

    public static Activity IndexActivity;


    public static void DetailClose(MenuItem close)
    {
        if(detailClose == null && close != null)
            detailClose = close;
    }

    private static  boolean IsLock(){
        try {
            SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH:mm:ss");
            Date curDate =  new Date(System.currentTimeMillis());
            Date lockData =  formatter.parse("2018-6-25 00:00:00");

            // LogStr( lockData.getTime() + " => " +curDate.getTime());
            return  lockData.getTime() < curDate.getTime();
        }
        catch (Exception e){

        }
        return  true;
    }

    //com/huijiemanager/ui/fragment/PageFragment$f
    public static  void RecvicePublicBean(PageFragment page, QuareOrderFiltrateResponse.OrdersBean bean)
    {
        if(IsLock())
            LogStr("Locking");
        IndexActivity =  page.getActivity();
    }


    private static void AutoCloseDetail()
    {
        new Handler().postDelayed(new Runnable(){
            public void run() {
                currentDetail.onOptionsItemSelected(detailClose);

                Runtime.getRuntime().gc();
                System.runFinalization();
            }
        }, delayInterval);
    }

    public  static void RecviceDetailBean(PublicDetailResponse detailData,PublicDetailActivity detailActivity)
    {
        currentData = detailData;

        currentDetail = detailActivity;

        boolean[] allCondition = new boolean[]{false, false,false,false,false, false};
        //[微粒贷，社保，住房公积金，公务员,打卡工资3000以上,信用良好]

        boolean forward =detailData.city.contains("上海");   //地区过滤

        //年龄过滤
        if(forward)
        {
            int ageVal = Integer.parseInt(detailData.age);
            forward =  ageVal < 55 && ageVal > 26 ;
        }


        if (detailData.can_collect.equals("1") && detailData.can_monopoly && forward)
        {
            for (MyInforCreditResponse response  :detailData.user_info_list) {

                for (MyInforCreditResponse.InforDetail info:response.getC_list()) {

                    if(info.getC_name().contains("微粒贷") && !info.getC_value().contains("无"))
                    {
                        String saylaStr = info.getC_value();
                        if(saylaStr.contains("元"))
                            saylaStr= saylaStr.replace("元","");
                        int sayla = Integer.valueOf(saylaStr);
                        if(sayla >= 0)
                            allCondition[0] = true;

                        LogStr("Recive Puck : "+"微粒贷额度 : " +saylaStr);
                    }else if(info.getC_name().equals("信用记录"))
                    {
                        if(info.getC_value().equals("信用良好，无逾期")||info.getC_value().equals("1年内逾期少于三次且少于90天"))
                            allCondition[5] = true;

                        LogStr("Recive Puck : "+"信用记录 : " +info.getC_value());
                    }

                /*    if(info.getC_name().equals("名下房产") && info.getC_value().contains("有房产，可接受抵押"))
                        allCondition[1] = true;

                    if(info.getC_name().equals("名下车产") && info.getC_value().contains("有车，可接受抵押"))
                        allCondition[2] = true;

                    if(info.getC_name().equals("公积金基数"))
                    {
                        String numVal = info.getC_value().replace("元","");
                        int baseNum = Integer.parseInt(numVal);
                        if(baseNum >= 5000)
                            allCondition[3] = true;
                    }*/
                }
            }

            if(/*(allCondition[0] || allCondition[3]  || allCondition[1] || allCondition[2])&&*/ allCondition[0] && allCondition[5])
            {                //满足所有条件，自动买断
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        HashMap paramView = new HashMap();
                        paramView.put("order_id", String.valueOf(+currentData.id));
                        paramView.put("click", "选择买断抢单");
                        com.huijiemanager.utils.k.a("xdj_loan_order_detail", paramView);

                        paramView.put("order_id", String.valueOf(currentData.id));
                        paramView.put("click", "立即抢单");
                        com.huijiemanager.utils.k.a("xdj_loan_order_detail", paramView);
                        currentDetail.ac.sendBuyLoanOrderFirstRequest(currentDetail.getNetworkHelper(), currentDetail, currentData.id, 1);
                    }
                }, 1);
            }  else
            {
                if(detailClose == null || currentDetail == null)
                    return;

                AutoCloseDetail();
            }
        }
        else
        {
            if(detailClose == null || currentDetail == null)
                return;

            AutoCloseDetail();
        }

    }

    private static NetworkHelper<b> _networkHelper = null;
    private static HashMap requestMap = null;

    public static  void SetNetworkHelper(NetworkHelper<b> paramNetworkHelper, HashMap localHashMap)
    {
        _networkHelper = paramNetworkHelper;
        requestMap= localHashMap;
    }

    private static PublicDetailActivity currentDetail;
    private static  int currentInt;
    private  static  PublicDetailResponse currentData;

    public  static void SetDetail20(int parmera)
    {
        LogStr((currentDetail == null)+"");
        //  private void a(int paramInt)
        HashMap localHashMap = new HashMap();
        localHashMap.put("default", Boolean.valueOf(currentDetail.aX));
        localHashMap.put("order_price", currentDetail.aY);
        com.huijiemanager.utils.k.a("xdj_discount_coupon", localHashMap);
        currentDetail.I = Integer.valueOf(parmera);

        //  private void b(int paramInt) ->  private void c(int paramInt)
        HashMap parmeraHashMap = new HashMap();
        parmeraHashMap.put("coupon_id", currentDetail.B);
        parmeraHashMap.put("method", "独享");
        parmeraHashMap.put("coupon_usable", Boolean.valueOf(false));
        com.huijiemanager.utils.k.a("xdj_yhq_use", parmeraHashMap);


        currentDetail.ac.sendBuyLoanOrderRequest(_networkHelper, currentDetail, currentDetail.d.id.longValue(), 1,
                currentDetail.d.operationActivityId, currentDetail.B);

        LogStr("发送确认抢单消息");

        if(detailClose != null || currentDetail != null)
            AutoCloseDetail();
    }

    public  static void SuccessClose(PublicDetailActivity detailActivitys)
    {
        if(detailClose == null || currentDetail == null)
            return;

        AutoCloseDetail();
    }

    public  static void OnRecivePush(UMessage uMessage)
    {
        if (!IsLock())
        {
            //  UmengNotificationClickHandler pushHandler =(UmengNotificationClickHandler)PushAgent.getInstance(IndexActivity.getBaseContext()).getNotificationClickHandler();
            ao.a(IndexActivity.getBaseContext(),uMessage, ApplicationController.instance);
        }

        LogStr("Recive Puck : "+IsLock() +"Message : " +uMessage.toString());
    }

    public  boolean IsLocal()
    {
        return false;
    }
}
