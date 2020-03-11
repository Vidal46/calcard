package br.com.calcard.android.app.ui.register;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.R;
import br.com.calcard.android.app.databinding.ActivityInputDataBinding;
import br.com.calcard.android.app.ui.login.LoginActivity;


public class InputDataActivity extends AppCompatActivity {
    InputDataViewModel inputDataViewModel;
    ActivityInputDataBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
        setStatusBarGradiant(this);
        inputDataViewModel = new InputDataViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_input_data);
        binding.setInputDataViewModel(inputDataViewModel);
        binding.getInputDataViewModel().token.set(getIntent().getStringExtra("tokenSMS"));
        binding.executePendingBindings();
        binding.getInputDataViewModel().cpf.set(getIntent().getStringExtra("cpf"));
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        bindingState();
        binding.getInputDataViewModel().forgot.set(getIntent().getBooleanExtra("isForgot", false));

    }

    public void onClick(View view) {
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(InputDataActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        binding.getInputDataViewModel().onClick(android_id);
    }

    public void bindingState() {
        binding.getInputDataViewModel().loadingState.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable sender, int propertyId) {
                        if (binding.getInputDataViewModel().loadingState.get()) {
                            progressDialog =
                                    ProgressDialog.show(InputDataActivity.this, "Registrando",
                                            "Aguarde...", true, false);
                        } else {
                            progressDialog.dismiss();

                        }
                    }
                });
        binding.getInputDataViewModel().passwordConfirm.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (binding.getInputDataViewModel().password.get().equals(binding.getInputDataViewModel().passwordConfirm.get())) {
                    binding.getInputDataViewModel().passwordMatch.set(true);
                } else {
                    binding.getInputDataViewModel().passwordMatch.set(false);
                }
            }
        });
        binding.getInputDataViewModel().wrongPassword.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (binding.getInputDataViewModel().wrongPassword.get()) {
                    Toast.makeText(InputDataActivity.this, "Senhas não são iguais", Toast.LENGTH_SHORT).show();
                }
                binding.getInputDataViewModel().wrongPassword.set(false);

            }

        });
        binding.getInputDataViewModel().isRegisterOk.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (binding.getInputDataViewModel().isRegisterOk.get().equals("ok")) {
                    Intent intent = new Intent(InputDataActivity.this, LoginActivity.class);
                    MyApplication.preferences.edit().putBoolean("isRegister", true).apply();
                    startActivity(intent);
                    finish();
                } else if (!binding.getInputDataViewModel().isRegisterOk.get().equals("")) {
                    Toast.makeText(InputDataActivity.this, binding.getInputDataViewModel().isRegisterOk.get(), Toast.LENGTH_SHORT).show();
                }
                binding.getInputDataViewModel().isRegisterOk.set("");
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

    @Override
    public void onStop() {
        MyApplication.preferences.edit().putBoolean("isForgot", false).apply();
        super.onStop();
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
