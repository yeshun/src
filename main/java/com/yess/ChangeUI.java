package com.yess;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.huijiemanager.ui.fragment.PageFragment;

/**
 * Created by yehun on 2018/5/20.
 */

public class ChangeUI {
    private static  TestSmali instance;
    public  void Test(PageFragment page){
        if(instance == null)
        {
            instance = new TestSmali();
            try{
                /*View view = page.getView().getRootView();
                RelativeLayout relative = new RelativeLayout(view.getContext());
                relative.setBackgroundColor(Color.YELLOW);

                // ��Button2 ���뵽RelativeLayout ��
                instance.button = new Button(view.getContext());
                instance.button.setText("����");//������ʾ���ַ�
                instance.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if( instance.button.getText().equals("����"))
                            instance.button.setText("�ر�");
                        else
                            instance.button.setText("����");
                    }
                });
                relative.addView( instance.button);
                // ����RelativeLayout���ֵĿ��
                RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                instance.button.setLayoutParams(lp);   ////���ð�ť�Ĳ�������
                page.getActivity().addContentView(relative,lp);*/
            }catch (Exception e){
            }
            // page.getActivity().addContentView(button, layout);
        }
    }
}