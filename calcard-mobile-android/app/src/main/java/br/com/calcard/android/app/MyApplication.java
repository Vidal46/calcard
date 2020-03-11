package br.com.calcard.android.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

import br.com.calcard.android.app.di.AppComponent;
import br.com.calcard.android.app.di.AppModule;
import br.com.calcard.android.app.di.DaggerAppComponent;

public class MyApplication extends Application {

    private static AppComponent component;
    private static Context context;


    public static SharedPreferences preferences;
    //FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        AppCenter.start(this, "38d548ce-6926-4c8b-8500-a614aae3e8c2", Analytics.class, Crashes.class);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        context = this;
        if (component == null) {
            component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        }
    }

    public static AppComponent getAppComponent() {
        return component;
    }

    public static Context getContext() {
        return context;
    }
}
