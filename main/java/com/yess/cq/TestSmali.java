package com.yess.cq;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by yehun on 2018/4/14.
 */
public class TestSmali {

    public static TestSmali instance;
    private Button button;
    private EditText editText;
    public Activity mainActivity;
    private RelativeLayout relative;
    private static  String TAG = "yess : ";
    public  static  void LogStr(String parmeras)
    {
        //Log.d(TAG,parmeras);
    }

    public static  boolean startAgent = false;

    public static  boolean autoCheck = false;

    private static MenuItem detailClose;

    private static int delayInterval = 600;

    public static Activity IndexActivity;
    private static PageFragment lastFragment;
    static Handler handler = new Handler();
    static Handler submitHandler = new Handler();
    static  Runnable requestDetail = null;
    static  Runnable requestList = null;
    static  Runnable autoClose = null;
    static QuareOrderFiltrateResponse.OrdersBean beanFrist;

    public static void DetailClose(MenuItem close)
    {
        if(IsLock())
            return;

        if(detailClose == null && close != null)
            detailClose = close;

        if(_networkHelper != null)
        {
            if(requestList == null)
            {
                requestList = new Runnable(){
                    public void run() {
                        lastFragment.a();
                        startAgent = true;
                        LogStr("自动发送获取新订单消息" );
                    }
                };
            }
            handler.postDelayed(requestList, delayInterval);
        }
    }

    private static  boolean IsLock(){
        try {
            if(instance == null)
                return  true;

            SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH:mm:ss");
            Date curDate =  new Date(System.currentTimeMillis());
            String dataStr = instance.activite.replace("Space"," ");
            Date lockData =  formatter.parse(dataStr);

            LogStr( "Lock : " +lockData.getTime() + " => " +curDate.getTime());
            return  lockData.getTime() < curDate.getTime() || !instance.IsLocal();
        }
        catch (Exception e){

        }
        return  true;
    }

    //com/huijiemanager/ui/fragment/PageFragment$f
    public static  void RecvicePublicBean(PageFragment page, List orderBeans)
    {
        lastFragment = page;
        IndexActivity =  page.getActivity();

        if(instance == null) {
            instance = new TestSmali();
            instance.mainActivity = page.getActivity();

            View view = page.getView().getRootView();
            instance.relative = new RelativeLayout(view.getContext());
            instance.relative.setBackgroundColor(Color.YELLOW);
            instance.relative.setY(200);

            instance.button = new Button(view.getContext());

            String conentStr = instance.CheckYessKeys(instance.mainActivity);
            instance.ParseYessKey(conentStr);
            if(IsLock())
                instance.button.setText("激活");
            else
                instance.button.setText("重新激活");

            instance.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (instance.button.getText().equals("激活")) {
                        if(instance.editText == null)
                            instance.InitEditText();
                        else
                            instance.editText.setEnabled(true);

                        instance.button.setText("保存");
                        instance.button.setEnabled(false);
                    }
                    else if (instance.button.getText().equals("重新激活")) {
                        if(instance.editText == null)
                            instance.InitEditText();
                        instance.editText.setText(instance.CheckYessKeys(instance.mainActivity));
                        instance.button.setText("保存");
                    }
                    else if(instance.button.getText().equals("保存"))
                    {
                        //解析验证安全码。成功则生成 CreateYessKeys 授权文件。
                        instance.ParseYessKey(instance.editText.getText().toString());
                        if(!IsLock())
                        {
                            instance.button.setText("重新激活");
                            instance.CreateYessKeys(instance.editText.getText().toString());
                        }else
                        {
                            //失败 重置 InitEditText
                            String ANDROID_ID = Settings.System.getString(IndexActivity.getContentResolver(), Settings.System.ANDROID_ID);
                            instance.editText.setText("本机设备码: \r\n" + ANDROID_ID);
                            instance.button.setText("激活");
                        }
                    }
                }
            });
            instance.relative.addView(instance.button);
            // ����RelativeLayout���ֵĿ��
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            //lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            instance.button.setLayoutParams(lp);   ////���ð�ť�Ĳ�������
            instance.mainActivity.addContentView(instance.relative, lp);
        }

        //收到order bean ,延迟查询详情
        if(!IsLock() &&orderBeans != null && orderBeans.size() >0 && detailClose != null){
            //开启自动请求最新订单详情任务.
            List<QuareOrderFiltrateResponse.OrdersBean> orderList = (List<QuareOrderFiltrateResponse.OrdersBean> )orderBeans;
          /*  ArrayList<QuareOrderFiltrateResponse.OrdersBean> arrayList = new ArrayList<QuareOrderFiltrateResponse.OrdersBean>();
            arrayList.addAll(orderList);
            arrayList.sort(new OrderComparator());*/

            Collections.sort(orderList, new Comparator<QuareOrderFiltrateResponse.OrdersBean>() {

                @Override
                public int compare(QuareOrderFiltrateResponse.OrdersBean itemBean1, QuareOrderFiltrateResponse.OrdersBean itemBean2) {
                    Long date1 = Long.parseLong(itemBean1.getCreateTime());
                    Long date2 = Long.parseLong(itemBean2.getCreateTime());
                    return date1 < date2 ? 1 : -1;
                }
            });
            beanFrist = orderList.get(0);
            if(beanFrist == null)
                return;
            if(requestDetail == null)
            {
                requestDetail = new Runnable(){
                    public void run() {
                        StringBuilder parmeras = new StringBuilder();
                        parmeras.append(beanFrist.getId());
                        parmeras.append("");

                        String parmera = parmeras.toString();

                        Intent intent = new Intent(lastFragment.getContext(),PublicDetailActivity.class);
                        intent.putExtra("id",parmera);

                        lastFragment.startActivityForResult(intent,0);
                    }
                };
            }
            handler.postDelayed(requestDetail, delayInterval);
        }
    }

    private static void AutoCloseDetail()
    {
        if(autoClose == null)
        {
            autoClose = new Runnable(){
                public void run() {
                    currentDetail.onOptionsItemSelected(detailClose);

                    Runtime.getRuntime().gc();
                    System.runFinalization();
                }
            };
        }
        handler.postDelayed(autoClose, delayInterval);
    }

    public  static void RecviceDetailBean(PublicDetailResponse detailData,PublicDetailActivity detailActivity)
    {
        if(IsLock())
            return;

        currentData = detailData;

        currentDetail = detailActivity;

        boolean[] allCondition = new boolean[]{false, false,false,false,false, false};
        //[微粒贷，社保，住房公积金，公务员,打卡工资3000以上,信用良好]

        boolean forward =detailData.city.contains("重庆");   //地区过滤

        //年龄过滤
        if(forward)
        {
            int ageVal = Integer.parseInt(detailData.age);
            forward =  ageVal < 58 && ageVal > 26 ;
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
                        if(sayla >= 10000)
                            allCondition[0] = true;

                        LogStr("Recive Puck : "+"微粒贷额度 : " +saylaStr);
                    }else if(info.getC_name().equals("本地公积金"))
                    {
                        if(info.getC_value().contains("连续6个月"))
                            allCondition[1] = true;
                    } else if(info.getC_name().equals("公积金基数"))
                    {
                        String gongjijStr = info.getC_value();
                        if(gongjijStr.contains("元"))
                            gongjijStr= gongjijStr.replace("元","");
                        int gonjj = Integer.valueOf(gongjijStr);
                        if(gonjj >= 5000)
                            allCondition[2] = true;
                    }else if(info.getC_name().equals("信用记录") && info.getC_value().equals("信用良好，无逾期"))
                        allCondition[5] = true;
                }
            }

            if((allCondition[0] || (allCondition[1]  &&allCondition[2]))&&allCondition[5])
            {                //满足所有条件，自动买断
                submitHandler.postDelayed(new Runnable(){
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
    public static  void SetNetworkHelper(NetworkHelper<b> paramNetworkHelper, HashMap localHashMap)
    {
        _networkHelper = paramNetworkHelper;
    }

    private static PublicDetailActivity currentDetail;
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
        final UMessage pushMessage = uMessage;
        if (!IsLock())
        {
            handler.removeCallbacks(requestDetail);
            handler.removeCallbacks(autoClose);
            handler.removeCallbacks(requestList);

            //当前在详情界面马上关闭详情开始解析推送订单
            if(currentDetail != null && detailClose != null)
                currentDetail.onOptionsItemSelected(detailClose);

            ao.a(IndexActivity.getBaseContext(),pushMessage, ApplicationController.instance);
        }

        LogStr("Recive Puck : "+IsLock() +"Message : " +uMessage.toString());
    }

    public void ParseYessKey(String yessKeys)
    {
        if(yessKeys.isEmpty())
            return;

        LogStr("Lock : " + yessKeys);
        try {
            String decodeAgin = Decrypt(yessKeys,"yeshun_296457808");
            String[] datas = decodeAgin.split("YesseY");
            if(datas.length==2)
            {
                LogStr("Lock : 1" + decodeAgin);
                LogStr("Lock : 2" + datas[0]);
                LogStr("Lock : 3" + datas[1]);
                instance.activite = datas[0];
                instance.resolverId = datas[1];
            }else
            {
                instance.activite = "1970-1-1Space00:00:00";
                instance.resolverId = "";
            }
        }
        catch (Exception e){
            instance.activite = "1970-1-1Space00:00:00";
            instance.resolverId = "";
            return;
        }

        return;
    }

    private String activite;
    private  String resolverId;

    public  boolean IsLocal()
    {
        String ANDROID_ID = Settings.System.getString(IndexActivity.getContentResolver(), Settings.System.ANDROID_ID);
        LogStr( "Lock : " +ANDROID_ID + " => " +instance.resolverId);
        return !instance.resolverId.isEmpty()&&ANDROID_ID.equals(instance.resolverId) ;
    }

    public static String Decrypt(String sSrc, String sKey) {
        try {
            // 判断Key是否正确
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
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = android.util.Base64.decode(sSrc, android.util.Base64.NO_WRAP);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    ///解析本地授权文件
    private String CheckYessKeys(Activity activity)
    {
        try {
            File yessKeys = new File(activity.getFilesDir(), "YessKeys.yes");
            if(yessKeys.exists())
            {
                FileInputStream is = new FileInputStream(yessKeys);
                int length = is.available();

                byte [] buffer = new byte[length];
                is.read(buffer);

                String res = new String(buffer, "UTF-8");

                is.close();
                return res;
            }else
                return "";
        }catch (Exception e){
            return "";
        }
    }

    private void InitEditText()
    {
        String ANDROID_ID = Settings.System.getString(IndexActivity.getContentResolver(), Settings.System.ANDROID_ID);
        instance.editText =new EditText(instance.button.getContext());
        instance.editText.setText("本机设备码: \r\n" + ANDROID_ID);
        Display display = IndexActivity.getWindowManager().getDefaultDisplay();
        instance.editText.setWidth(display.getWidth() - 100);
        instance.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                instance.button.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        instance.editText.setLayoutParams(lp);
        instance.editText.setX(100);
        instance.relative.addView( instance.editText,lp);
        instance.relative.refreshDrawableState();
    }

    private void CreateYessKeys(String contents)
    {
        try {
            File yessKeys = new File( instance.mainActivity.getFilesDir(), "YessKeys.yes");
            yessKeys.createNewFile();
            FileOutputStream os = new FileOutputStream(yessKeys);
            os.write(contents.getBytes());
            os.close();
        }catch (Exception e){
        }
    }
}
