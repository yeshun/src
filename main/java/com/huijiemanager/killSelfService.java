package com.huijiemanager;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.yess.TestSmali;

public class killSelfService extends Service {

   // private static  long stopDelayed=2000;
    private Handler handler;
    private String PackageName;
    public killSelfService() {
        handler=new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        //stopDelayed=intent.getLongExtra("Delayed",2000);
       // PackageName=intent.getStringExtra("PackageName");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                TestSmali.startAgent = true;
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.huijiemanager");
                startActivity(LaunchIntent);
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
