package com.huijiemanager;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.yess.TestSmali;

public class killSelfService extends Service {

   // private static  long stopDelayed=2000;
    private Handler handler;
    private String PackageName;

    private TestSmali instance;
    public killSelfService() {
        handler=new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        //stopDelayed=intent.getLongExtra("Delayed",2000);
       PackageName=intent.getStringExtra("PackageName");

        instance = TestSmali.instance;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(PackageName);
                startActivity(LaunchIntent);
                TestSmali.startAgent = true;
                TestSmali.autoCheck = true;
                killSelfService.this.stopSelf();

            }
        },2000L);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
