package br.com.calcard.android.app.ui.cards;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import java.util.Objects;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {
    ChangePasswordViewModel viewModel;
    ActivityChangePasswordBinding binding;
    private ProgressDialog progressDialog;
    String deviceId;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setStatusBarGradiant(this);
        deviceId= Settings.Secure.getString(ChangePasswordActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        viewModel= new ChangePasswordViewModel();
        binding= DataBindingUtil.setContentView(this,R.layout.activity_change_password);
        binding.setViewChangePassword(viewModel);
        binding.executePendingBindings();
        binding.layout.clearFocus();
        binds();
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }
    public void onChangeClick(View view){
       binding.textInputLayout4.setError(null);
       binding.textInputLayout5.setError(null);
       binding.textInputLayout6.setError(null);
       if (TextUtils.isEmpty(binding.textInputLayout4.getEditText().getText())){
           binding.textInputLayout4.setError("Preencha a sua senha");
        }
        else if (TextUtils.isEmpty(binding.textInputLayout5.getEditText().getText())){
           binding.textInputLayout5.setError("Preencha sua nova senha");
       }
       else if(TextUtils.isEmpty(binding.textInputLayout6.getEditText().getText())){
           binding.textInputLayout6.setError("Confirme sua nova senha");
       }else if (!isPasswordValid(binding.textInputLayout5.getEditText().getText().toString())){
           binding.textInputLayout5.setError("Senha deve conter 8 caracteres");
       }else if (!isPasswordEquals(binding.textInputLayout5.getEditText().getText().toString(),binding.textInputLayout6.getEditText().getText().toString())){
           binding.textInputLayout6.setError("Senhas nao conferem");
       }else {
           binding.layout.clearFocus();
//           binding.getViewChangePassword().changePassword(binding.textInputLayout4.getEditText().getText().toString(),binding.textInputLayout5.getEditText().getText().toString(),deviceId);
       }
       }
       public void binds(){
           binding.getViewChangePassword().loadingState.addOnPropertyChangedCallback(
                   new Observable.OnPropertyChangedCallback() {
                       @Override
                       public void onPropertyChanged(Observable observable, int i) {
                           boolean isLoading = binding.getViewChangePassword().loadingState.get();
                           if (isLoading){
                               progressDialog =
                                       ProgressDialog.show(ChangePasswordActivity.this,"Alterando Senha",
                                               "Aguarde...",true, false);
                           } else {
                               progressDialog.dismiss();

                           }
                       }
                   });

           binding.getViewChangePassword().isChangeDone.addOnPropertyChangedCallback(
                   new Observable.OnPropertyChangedCallback() {
                       @Override
                       public void onPropertyChanged(Observable observable, int i) {
                           String isLoginDone = binding.getViewChangePassword().isChangeDone.get();
                           if (isLoginDone.equals("true")) {
                              Toast.makeText(ChangePasswordActivity.this,"Senha Alterada com sucesso",Toast.LENGTH_SHORT).show();
                              startActivity(new Intent(ChangePasswordActivity.this,HomeActivity.class));
                              }else if (isLoginDone.equals("false")){
                               Toast.makeText(ChangePasswordActivity.this,"Senha Incorreta",Toast.LENGTH_SHORT).show();
                               }
                               binding.getViewChangePassword().isChangeDone.set("");
                       }
                   });
       }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }
    private boolean isPasswordEquals(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.parseColor("#2D2553"));
        }
    }

}
