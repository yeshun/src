package com.yess;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.huijiemanager.base.b;
import com.huijiemanager.http.NetworkHelper;
import com.huijiemanager.http.response.MyInforCreditResponse;
import com.huijiemanager.http.response.PublicDetailResponse;
import com.huijiemanager.http.response.QuareOrderFiltrateResponse;
import com.huijiemanager.killSelfService;
import com.huijiemanager.ui.activity.PublicDetailActivity;
import com.huijiemanager.ui.fragment.PageFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yehun on 2018/4/14.
 */
public class TestSmali {

    public  static  TestSmali getInstance(){
        if(instance == null)
            instance = new TestSmali();

        return instance;
    }

    public static  TestSmali instance;
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

    private static MenuItem detailClose;

    private  static  ArrayList<Integer> rededOrders = new ArrayList<Integer>();
    public static ArrayList<QuareOrderFiltrateResponse.OrdersBean> allOrder = new ArrayList<QuareOrderFiltrateResponse.OrdersBean>();
    public static HashMap<QuareOrderFiltrateResponse.OrdersBean,PageFragment> allPage = new HashMap<QuareOrderFiltrateResponse.OrdersBean,PageFragment>();
    // private static int queryCounter = 5;

    private static PageFragment lastFragment;

    private List<OrdreFilter> filters;

    //  private static ScheduledThreadPoolExecutor pool;

    private static int delayInterval = 60;

    private int orderCount;

    private static int autoCount = 80;

    public static void DetailClose(MenuItem close)
    {

        if(detailClose == null && close != null)
            detailClose = close;

        boolean autoRequest = false;

        //查找当前public bean里面没有查询过的订单
        QuareOrderFiltrateResponse.OrdersBean beanUnRed = null;
        for (QuareOrderFiltrateResponse.OrdersBean bean :allOrder) {
            if (!rededOrders.contains(bean.getId()))
            {
                beanUnRed = bean;
                break;
            }
        }

        if(beanUnRed != null && instance.button.getText().equals("暂停"))
        {
            lastFragment = allPage.get(beanUnRed);
            if(lastFragment != null )
            {
                rededOrders.add(beanUnRed.getId());
                StringBuilder parmeras = new StringBuilder();
                parmeras.append(beanUnRed.getId());
                parmeras.append("");

                String parmera = parmeras.toString();

                Intent intent = new Intent(lastFragment.getContext(),PublicDetailActivity.class);
                intent.putExtra("id",parmera);

                lastFragment.startActivityForResult(intent,0);

                allOrder.remove(beanUnRed);
                allPage.remove(beanUnRed);
                // queryCounter --;
                LogStr("自动检查下一个订单 ：" +beanUnRed.getUserDesc() +" size : " +allOrder.size());
            }else
                autoRequest = true;

        }else
            autoRequest = true;

        if(autoRequest && instance.button.getText().equals("暂停"))
        {
            if(instance.orderCount > autoCount)
            {
                LogStr("AUTO CLOSE");
                Context context = instance.mainActivity.getBaseContext();
                Intent intent1=new Intent(context,killSelfService.class);
                intent1.putExtra("PackageName",context.getPackageName());
                intent1.putExtra("Delayed",2000);
                context.startService(intent1);

                allOrder.clear();
                allPage.clear();
                rededOrders.clear();
                instance.orderCount = 0;
                instance.mainActivity.finish();
                startAgent = true;
                instance = null;
            }else{
                if(instance.button.getText().equals("暂停"))
                    RequestOrderList();
            }
        }
    }

    public static void RequestOrderList()
    {
        if(_networkHelper != null && requestMap != null)
        {
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    lastFragment.a();
                    allOrder.clear();
                    allPage.clear();
                    startAgent = true;
                    LogStr("自动发送获取新订单消息" );
                }
            }, delayInterval);
        }
    }

    private static  boolean IsLock(String  lockStr){
        try {
            SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH:mm:ss");
            Date curDate =  new Date(System.currentTimeMillis());
            Date lockData =  formatter.parse(lockStr);
            return  lockData.getTime() < curDate.getTime();
        }
        catch (Exception e){

        }
        return  true;
    }

    //com/huijiemanager/ui/fragment/PageFragment$f
    public static  void RecvicePublicBean(PageFragment page, QuareOrderFiltrateResponse.OrdersBean bean)
    {
        allOrder.add(bean);
        allPage.put(bean,page);
        try {
            if(instance == null) {
                instance = new TestSmali();
                instance.mainActivity = page.getActivity();

                View view = page.getView().getRootView();
                instance.relative = new RelativeLayout(view.getContext());
                instance.relative.setBackgroundColor(Color.YELLOW);
                instance.relative.setY(200);

                instance.button = new Button(view.getContext());

                instance.filters = instance.ParseYessKey(instance.CheckYessKeys(instance.mainActivity));
                if(instance.filters == null)
                    instance.button.setText("激活");
                else
                    instance.button.setText("开始");
                instance.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (instance.button.getText().equals("激活")) {
                            instance.button.setEnabled(false);
                            if(instance.editText == null)
                                instance.InitEditText();
                            else
                                instance.editText.setEnabled(true);

                            instance.button.setText("保存");
                        }
                        else if (instance.button.getText().equals("开始")) {
                            instance.button.setText("暂停");

                            //激活机器人自动接单流程
                            if(instance.editText == null)
                                instance.InitEditText();
                            instance.editText.setText(instance.CheckYessKeys(instance.mainActivity));
                            instance.editText.setEnabled(false);
                            instance.filters = instance.ParseYessKey(instance.editText.getText().toString());

                            QuareOrderFiltrateResponse.OrdersBean orderBean = allOrder.get(0);
                            lastFragment = allPage.get(orderBean);
                            if(!rededOrders.contains(orderBean.getId()))
                                rededOrders.add(orderBean.getId());

                            startAgent = false;
                            StringBuilder parmeras = new StringBuilder();
                            parmeras.append(orderBean.getId());
                            parmeras.append("");

                            String parmera = parmeras.toString();

                            Intent intent = new Intent(lastFragment.getContext(),PublicDetailActivity.class);
                            intent.putExtra("id",parmera);

                            lastFragment.startActivityForResult(intent,0);

                            LogStr("开始检查第一个订单 ：" +orderBean.getUserDesc());
                        }
                        else if(instance.button.getText().equals("保存"))
                        {
                            //解析验证安全码。成功则生成 CreateYessKeys 授权文件。
                            List<OrdreFilter> readDatas = instance.ParseYessKey(instance.editText.getText().toString());
                            if(  readDatas != null)
                            {
                                instance.button.setText("开始");
                                instance.CreateYessKeys(instance.editText.getText().toString());
                            }else
                            {
                                //失败 重置 InitEditText
                                instance.editText.setText("            输入激活码....没有激活码联系开发人员索取 \r\n");
                                instance.button.setEnabled(false);
                            }
                        }
                        else if(instance.button.getText().equals("暂停"))
                        {
                            instance.button.setText("开始");
                            instance.editText.setEnabled(true);
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

            else if(instance.button.getText().equals("暂停") && startAgent){
                startAgent = false;
                QuareOrderFiltrateResponse.OrdersBean orderBean = allOrder.get(0);
                lastFragment = allPage.get(orderBean);
                if(!rededOrders.contains(orderBean.getId()))
                    rededOrders.add(orderBean.getId());

                startAgent = false;
                StringBuilder parmeras = new StringBuilder();
                parmeras.append(orderBean.getId());
                parmeras.append("");

                String parmera = parmeras.toString();

                Intent intent = new Intent(lastFragment.getContext(),PublicDetailActivity.class);
                intent.putExtra("id",parmera);

                lastFragment.startActivityForResult(intent,0);

                LogStr("开始检查第一个订单 ：" +orderBean.getUserDesc());
            }
        }catch (Exception e){
        }
    }

    private void InitEditText()
    {
        instance.editText =new EditText(instance.button.getContext());
        instance.editText.setText("            输入激活码....没有激活码联系开发人员索取 \r\n");
        instance.editText.setWidth(800);
        instance.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // instance.button.setText("保存");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
       /*         if(!instance.button.getText().equals("保存"))
                    instance.button.setText("保存");*/
                instance.button.setEnabled(count > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        //lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    private static void AutoCloseDetail()
    {
        if(detailClose == null || currentDetail == null)
            return;

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
        instance.orderCount++;
        currentData = detailData;
        currentDetail = detailActivity;

        boolean bSubmit = false;
        if(instance.filters == null)
            return;

        for (OrdreFilter filter : instance.filters)
        {
            boolean bCity = detailData.city.contains(filter.cityFlag);
            int ageVal = Integer.parseInt(detailData.age);
            boolean bAge = ageVal <= filter.maxAge && ageVal >= filter.minAge ;
            boolean canMonopoly = detailData.can_collect.equals("1") && detailData.can_monopoly;
            boolean bLock = IsLock(filter.lockFlag);
            if(bLock)
            {
                instance.CreateYessKeys("服务到期，请联系管理员续费");
                instance.button.setText("激活");
            }

            if(!bCity ||!bAge ||!canMonopoly||bLock)
            {
                bSubmit = false;
                break;
            }

            boolean allCondition = true;
            for (MyInforCreditResponse response  :detailData.user_info_list) {
                for (MyInforCreditResponse.InforDetail info:response.getC_list()) {

                    if (!filter.月收入.contains("无") &&info.getC_name().equals("月收入"))
                    {
                        String 收入字符 = info.getC_value().replace("元","");
                        int 收入 =Integer.valueOf(收入字符);
                        int 目标收入 =Integer.valueOf(filter.月收入);
                        if (收入 < 目标收入)
                        {
                            allCondition = false;
                            break;
                        }
                    }

                    if (!filter.本地社保.contains("无") &&info.getC_name().equals("本地社保"))
                    {
                        if (!info.getC_value().equals(filter.本地社保))
                        {
                            allCondition = false;
                            break;
                        }
                    }

                    if (!filter.本地公积金.contains("无") &&info.getC_name().equals("本地公积金"))
                    {
                        if (!info.getC_value().equals(filter.本地公积金))
                        {
                            allCondition = false;
                            break;
                        }
                    }

                    if (!filter.当前单位工龄.contains("无") &&info.getC_name().equals("当前单位工龄"))
                    {
                        if (!info.getC_value().equals(filter.当前单位工龄))
                        {
                            allCondition = false;
                            break;
                        }
                    }

                    if (!filter.手机归属地.contains("无") &&info.getC_name().equals("手机归属地"))
                    {
                        if (!info.getC_value().contains(filter.手机归属地))
                        {
                            allCondition = false;
                            break;
                        }
                    }

                    if (!filter.户籍所在地.contains("无") &&info.getC_name().equals("户籍所在地"))
                    {
                        if (!info.getC_value().contains(filter.户籍所在地))
                        {
                            allCondition = false;
                            break;
                        }
                    }

                    if (!filter.信用卡额度.contains("无") &&info.getC_name().equals("信用卡额度"))
                    {
                        String 收入字符 = info.getC_value().replace("元","");
                        int 收入 =Integer.valueOf(收入字符);
                        int 目标收入 =Integer.valueOf(filter.信用卡额度);
                        if (收入 < 目标收入)
                        {
                            allCondition = false;
                            break;
                        }
                    }

                    if (!filter.信用记录.contains("无") &&info.getC_name().equals("信用记录"))
                    {
                        if (!filter.信用记录.contains(info.getC_value()))
                        {
                            allCondition = false;
                            break;
                        }
                    }

                    if (!filter.微粒贷额度.contains("无") &&info.getC_name().equals("微粒贷额度"))
                    {
                        String 收入字符 = info.getC_value().replace("元","");
                        int 收入 =Integer.valueOf(收入字符);
                        int 目标收入 =Integer.valueOf(filter.微粒贷额度);
                        if (收入 < 目标收入)
                        {
                            allCondition = false;
                            break;
                        }
                    }

                    if (!filter.名下房产.contains("无") &&info.getC_name().equals("名下房产"))
                    {
                        if (!filter.名下房产.contains(info.getC_value()))
                        {
                            allCondition = false;
                            break;
                        }
                    }

                    if (!filter.名下车产.contains("无") &&info.getC_name().equals("名下车产"))
                    {
                        if (!filter.名下车产.contains(info.getC_value()))
                        {
                            allCondition = false;
                            break;
                        }
                    }

                    if (!filter.保单价值.contains("无") &&info.getC_name().equals("保单价值"))
                    {
                        String 收入字符 = info.getC_value().replace("万","");
                        int 收入 =Integer.valueOf(收入字符);
                        int 目标收入 =Integer.valueOf(filter.微粒贷额度.replace("万",""));
                        if (收入 < 目标收入)
                        {
                            allCondition = false;
                            break;
                        }
                    }

                }
            }

            if(allCondition)
            {
                bSubmit = true;
                break;
            }else{
                continue;
            }
        }

            if(bSubmit)
            {
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
                }, delayInterval);
            }else
                AutoCloseDetail();
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

        class OrdreFilter{

            public String  cityFlag;
            public String lockFlag;
            public int minAge;
            public int maxAge;

            public String 月收入;  //xx元
            public String 收入形式; //转账工资，现金发放，银行转账
            public String 本地社保;//无本地社保，连续6个月
            public String 本地公积金;//无本地公积金，连续6个月
            public String 当前单位工龄;//6个月以上
            public String 手机归属地;//市级包含检查
            public String 户籍所在地;//市级包含检查
            public String 信用卡额度;//xx元
            public String 信用记录;//信用良好无逾期，1年内逾期少于3次且少于90天,1年内逾期大于3次且大于90天
            public String 微粒贷额度;//xxx元
            public  String 名下房产;//有房产,不接受抵押
            public  String 名下车产;//有车产,不接受抵押
            public  String 保单价值;//有房产,不接受抵押
        }

        @TargetApi(Build.VERSION_CODES.O)
        public List<OrdreFilter> ParseYessKey(String yessKeys)
        {
            List<OrdreFilter> valiData = null;
            if(yessKeys.isEmpty())
                return  null;

            try {
                String decodeAgin = new String( android.util.Base64.decode(yessKeys, android.util.Base64.NO_WRAP));
            /*

            上海~2018-5-23Space00:00:00!22L50@3000#银行转账;连续6个月%连续6个月^6个月以上&上海*上海(8000)1年内逾期少于3次且少于90天_10000+有房产,可接受抵押{有车产,不接受抵押}30万||3001#现金发放;连续3个月%连续2个月^12个月以上&珠海*深圳(8888)2年内逾期少于4次且少于93天_10001+有房产,可接受抵押{有车产,不接受抵押}33万
            */

                OrdreFilter headData = new OrdreFilter();
                headData.cityFlag = decodeAgin.split("M")[0];
                decodeAgin = decodeAgin.replace(headData.cityFlag+"M","");
                String dataStr = decodeAgin.split("N")[0];
                headData.lockFlag =dataStr .replace("Space"," ");
                decodeAgin = decodeAgin.replace(dataStr+"N","");
                String ageStr =  decodeAgin.split("O")[0];
                if(!ageStr.isEmpty())
                {
                    String[] ageArray = ageStr.split("L");
                    headData.minAge = Integer.parseInt(ageArray[0]);
                    headData.maxAge = Integer.parseInt(ageArray[1]);
                }
                decodeAgin = decodeAgin.replace(ageStr+"O","");

                String[] filterArr = decodeAgin.split("\\|\\|");

                valiData = new ArrayList<OrdreFilter>();
                if(filterArr.length  == 0)
                {
                    valiData.add(headData);
                    return  valiData;
                }

                for (String filter:filterArr) {
                    //000#银行转账;连续6个月%连续6个月^6个月以上&上海*上海(8000)1年内逾期少于3次且少于90天_10000+有房产,可接受抵押{有车产,不接受抵押}30
                    OrdreFilter order = new OrdreFilter();
                    order.cityFlag = headData.cityFlag;
                    order.lockFlag = headData.lockFlag;
                    order.minAge = headData.minAge;
                    order.maxAge = headData.maxAge;

                    order.月收入 =  filter.split("#")[0];
                    filter = filter.replace(order.月收入 +"#","");

                    order.收入形式 =  filter.split("A")[0];
                    filter = filter.replace(order.收入形式 +"A","");

                    order.本地社保 =  filter.split("B")[0];
                    filter = filter.replace(order.本地社保 +"B","");

                    order.本地公积金 =  filter.split("C")[0];
                    filter = filter.replace(order.本地公积金 +"C","");

                    order.当前单位工龄 =  filter.split("D")[0];
                    filter = filter.replace(order.当前单位工龄 +"D","");

                    order.手机归属地 =  filter.split("E")[0];
                    filter = filter.replace(order.手机归属地 +"E","");

                    order.户籍所在地 =  filter.split("F")[0];
                    filter = filter.replace(order.户籍所在地 +"F","");

                    order.信用卡额度 =  filter.split("G")[0];
                    filter = filter.replace(order.信用卡额度 +"G","");

                    order.信用记录 =  filter.split("H")[0];
                    filter = filter.replace(order.信用记录 +"H","");

                    order.微粒贷额度 =  filter.split("I")[0];
                    filter = filter.replace(order.微粒贷额度 +"I","");

                    order.名下房产 =  filter.split("J")[0];
                    filter = filter.replace(order.名下房产 +"J","");

                    order.名下车产 =  filter.split("K")[0];
                    filter = filter.replace(order.名下车产 +"K","");

                    order.保单价值 =  filter;
                    valiData.add(order);
                }
            }
            catch (Exception e){

                LogStr(         e.getStackTrace().toString());
                return null;
            }

            return  valiData;
        }
    }
