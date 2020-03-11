package br.com.calcard.android.app.ui.onboard;


import br.com.calcard.android.app.MyApplication;

public class OnboardViewModel {


    boolean getFirstRun() {
        return MyApplication.preferences.getBoolean("firstRun", true);
    }

    void setFirstRun() {
        MyApplication.preferences.edit().putBoolean("firstRun", false).apply();

    }

    boolean getRegisterOk() {
        return MyApplication.preferences.getBoolean("isRegister", false);
    }
}
