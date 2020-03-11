package br.com.calcard.android.app.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.R;
import br.com.calcard.android.app.ui.invoice.InvoiceActivity;
import br.com.calcard.android.app.ui.login.LoginRedirectionActivity;
import br.com.calcard.android.app.ui.onboard.OnboardActivity;
import br.com.calcard.android.app.ui.redirection.MainRedirectionActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setStatusBarGradiant(this);
        FirebaseApp.initializeApp(this);


        int SPLASH_TIME_OUT = 900;
        new Handler().postDelayed(() -> {
            if (getFirstRun()){
                Intent i = new Intent(SplashActivity.this, OnboardActivity.class);
                startActivity(i);
            }else if (getRegisterOk()){
                Intent i = new Intent(SplashActivity.this, LoginRedirectionActivity.class);
                startActivity(i);
            }else {
                Intent i = new Intent(SplashActivity.this, MainRedirectionActivity.class);
                startActivity(i);
            }
            finish();
        }, SPLASH_TIME_OUT);

        AppCenter.start(getApplication(), "38d548ce-6926-4c8b-8500-a614aae3e8c2",
                Analytics.class, Crashes.class);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.parseColor("#2D2553"));
        }
    }


    boolean getFirstRun() {
        return MyApplication.preferences.getBoolean("firstRun", true);
    }

    boolean getRegisterOk(){
        return   MyApplication.preferences.getBoolean("isRegister",false);
    }

}
