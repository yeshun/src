package com.yess.nj;

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
        Log.d(TAG,parmeras);
    }

    public static  boolean startAgent = false;

    public static  boolean autoCheck = false;

    private static MenuItem detailClose;

    private static int delayInterval = 600;

    public static Activity IndexActivity;


    public static void DetailClose(MenuItem close)
    {
        if(detailClose == null && close != null)
            detailClose = close;
    }

    private static  boolean IsLock(){
        try {
            if(instance == null)
                return  true;

            SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH:mm:ss");
            Date curDate =  new Date(System.currentTimeMillis());
            String dataStr = instance.activite.replace("Space"," ");
            Date lockData =  formatter.parse(dataStr);

            // LogStr( lockData.getTime() + " => " +curDate.getTime());
            return  lockData.getTime() < curDate.getTime() || !instance.IsLocal();
        }
        catch (Exception e){

        }
        return  true;
    }

    //com/huijiemanager/ui/fragment/PageFragment$f
    public static  void RecvicePublicBean(PageFragment page, QuareOrderFiltrateResponse.OrdersBean bean)
    {
     /*   if(IsLock())
            LogStr("Locking");*/
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
                        //instance.editText.setEnabled(false);
                    }
                    else if(instance.button.getText().equals("保存"))
                    {
                        //解析验证安全码。成功则生成 CreateYessKeys 授权文件。
                        instance.ParseYessKey(instance.editText.getText().toString());
                        if( !IsLock())
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
        if(IsLock())
            return;

        currentData = detailData;

        currentDetail = detailActivity;

        boolean[] allCondition = new boolean[]{false, false,false,false,false, false};
        //[微粒贷，社保，住房公积金，公务员,打卡工资3000以上,信用良好]

        boolean forward =detailData.city.contains("南京");   //地区过滤

        //年龄过滤
        if(forward)
        {
            int ageVal = Integer.parseInt(detailData.age);
            forward =  ageVal < 53 && ageVal > 23 ;
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
                    } if(info.getC_name().equals("本地社保"))
                    {
                        if(info.getC_value().contains("连续6个月"))
                            allCondition[2] = true;
                    }else if(info.getC_name().equals("房产类型") )
                    {
                        if(info.getC_value().contains("按揭房"))
                            allCondition[3] = true;
                    }
                    else if(info.getC_name().equals("保单价值") )
                    {
                        if(info.getC_value().contains("万及以上")) {
                            String saylaStr = info.getC_value();
                            saylaStr= saylaStr.replace("万及以上","");
                            int sayla = Integer.valueOf(saylaStr);
                            if(sayla >= 30)
                                allCondition[4] = true;
                        }
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

            if(allCondition[0] || (allCondition[1]  &&allCondition[2]) ||allCondition[3] || allCondition[4])
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
        if (!IsLock())
        {
            //  UmengNotificationClickHandler pushHandler =(UmengNotificationClickHandler)PushAgent.getInstance(IndexActivity.getBaseContext()).getNotificationClickHandler();
            ao.a(IndexActivity.getBaseContext(),uMessage, ApplicationController.instance);
        }

        LogStr("Recive Puck : "+IsLock() +"Message : " +uMessage.toString());
    }

    public void ParseYessKey(String yessKeys)
    {
        if(yessKeys.isEmpty())
            return;

        try {
            String decodeAgin = Decrypt(yessKeys,"yeshun_296457808");
            String[] datas = decodeAgin.split("YesseY");
            if(datas.length==2)
            {
                instance.activite = datas[0];
                instance.resolverId = datas[1];
            }else
            {
                instance.activite = "1970-1-1Space00:00:00";
                instance.resolverId = "";
            }
        }
        catch (Exception e){

            LogStr(e.getStackTrace().toString());
            return;
        }

        return;
    }

    private String activite;
    private  String resolverId;

    public  boolean IsLocal()
    {
        String ANDROID_ID = Settings.System.getString(IndexActivity.getContentResolver(), Settings.System.ANDROID_ID);
        return !resolverId.isEmpty()&&ANDROID_ID.equals(resolverId) ;
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
                instance.button.setEnabled(count > 0);
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
