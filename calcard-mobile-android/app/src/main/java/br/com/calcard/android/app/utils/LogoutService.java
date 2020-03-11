package br.com.calcard.android.app.utils;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import br.com.calcard.android.app.ui.login.LoginRedirectionActivity;

public class LogoutService extends Service {
    public static CountDownTimer timer;
    public static Boolean isActive = true;
    public static Boolean isBackground = false;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        timer = new CountDownTimer(5*60*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                isActive = true;
            }

            public void onFinish() {
                Log.v("SERVICELOGOUT", "Service logout");
                isActive = false;
                if(!isBackground) {
                    isActive = true;
                    Intent intent = new Intent(LogoutService.this, LoginRedirectionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                stopSelf();
            }
        };

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


