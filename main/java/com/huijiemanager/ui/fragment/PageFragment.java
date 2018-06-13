package com.huijiemanager.ui.fragment;


import android.support.v7.widget.RecyclerView;

import com.huijiemanager.app.ApplicationController;
import com.huijiemanager.base.BaseFragment;
import com.huijiemanager.http.response.QuareOrderFiltrateResponse;
import com.huijiemanager.http.response.QuareOrderTagsResponse;
import com.huijiemanager.ui.a.m;
import com.yess.TestSmali;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yehun on 2018/4/15.
 */

public class PageFragment extends BaseFragment {

    public QuareOrderTagsResponse n;
    public String j;
    public String k;
    public int l;
    public int m;
    private ArrayList t = new ArrayList();
    private RecyclerView q;

    public  static void e(PageFragment page){}

    public  void a(){}

 public  class f
 {
   public PageFragment p;

 }

    public void refreshItem(com.huijiemanager.ui.a.m var1) {
        String var4 = var1.a();
        Iterator var2 = this.t.iterator();

        PageFragment a = this;
        List b = getList();
        List c = b;
        TestSmali.RecvicePublicBean(a,c);

        while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 instanceof QuareOrderFiltrateResponse.OrdersBean) {
                QuareOrderFiltrateResponse.OrdersBean var5 = (QuareOrderFiltrateResponse.OrdersBean)var3;
                if (String.valueOf(var5.getId()).equals(var4)) {
                    var5.setCanCollect("0");
                    this.q.getAdapter().notifyDataSetChanged();
                    break;
                }
            }
        }

    }

    private List getList(){
        return null;
    }
}
