package br.com.calcard.android.app.ui.login;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.R;
import br.com.calcard.android.app.biometric.BiometricCallback;
import br.com.calcard.android.app.biometric.BiometricManager;
import br.com.calcard.android.app.databinding.ActivityLoginBinding;
import br.com.calcard.android.app.ui.cards.HomeActivity;
import br.com.calcard.android.app.ui.redirection.MainRedirectionActivity;
import br.com.calcard.android.app.ui.register.RegisterViewModel;
import br.com.calcard.android.app.ui.smsverification.OuzeSmsConfirmationActivity;
import br.com.calcard.android.app.utils.Constants;
import br.com.calcard.android.app.utils.LogoutService;
import br.com.calcard.android.app.utils.Mask;

public class LoginActivity extends AppCompatActivity implements BiometricCallback {

    private LoginViewModel loginViewModel;
    private RegisterViewModel registerViewModel;
    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;
    private String cpfIntent;
    String deviceId;
    AlertDialog alert;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatusBarGradiant(this);
        if (getIntent() != null) {
            cpfIntent = getIntent().getStringExtra("cpf");
        }

        loginViewModel = new LoginViewModel();
        registerViewModel = new RegisterViewModel();
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        deviceId = Settings.Secure.getString(LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.i("deviceId", deviceId);
        binding.setLoginViewModel(loginViewModel);
        binding.executePendingBindings();
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.editTextCPF.addTextChangedListener(Mask.insert("###.###.###-##", binding.editTextCPF));

        bindLoadingState();
        binding.getLoginViewModel().setBiometric();
        binding.switch1.setChecked(binding.getLoginViewModel().loginBiometric.get());
        String cpf = MyApplication.preferences.getString("cpf", null);
        binding.getLoginViewModel().cpf.set(cpf);
        if (cpf != null) {
            binding.editTextCPF.setEnabled(false);
            binding.editTextCPF.setTextColor(getResources().getColor(R.color.cinza));
            binding.textView86.setVisibility(View.VISIBLE);
        } else {
            binding.getLoginViewModel().cpf.set("");
        }
        if (cpfIntent != null) {
            binding.editTextCPF.setText(cpfIntent);
            binding.editTextCPF.setEnabled(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) LoginActivity.this.getSystemService(Context.FINGERPRINT_SERVICE);
            if (fingerprintManager != null) {
                if (!fingerprintManager.isHardwareDetected() || !fingerprintManager.hasEnrolledFingerprints()) {
                    binding.textView85.setVisibility(View.GONE);
                    binding.switch1.setVisibility(View.GONE);
                }
            }

        }
        binding.editTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && binding.getLoginViewModel().loginBiometric.get()) { }
        });
        binding.textView86.setOnClickListener(v -> {
            new AlertDialog.Builder(this, R.style.DialogTheme)
                    .setMessage("Deseja descadastrar sua conta atual?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", (dialog, id) -> clearApp())
                    .setNegativeButton("Não", null)
                    .show();

        });
    }

    public void clearApp() {
        MyApplication.preferences.edit().putString("cpf", null).apply();
        MyApplication.preferences.edit().putString("version", null).apply();
        MyApplication.preferences.edit().putBoolean("firstLogin", true).apply();
        MyApplication.preferences.edit().putString("tokenUser", null).apply();
        MyApplication.preferences.edit().putBoolean("isRegister", false).apply();
        MyApplication.preferences.edit().putBoolean("loginBiometric", false).apply();
        MyApplication.preferences.edit().putBoolean("generateFirebaseToken", false).apply();
        MyApplication.preferences.edit().putString("idCard", null).apply();
        MyApplication.preferences.edit().putString("statusCard", null).apply();
        MyApplication.preferences.edit().putString("card", null).apply();
        MyApplication.preferences.edit().putString("name", null).apply();
        MyApplication.preferences.edit().putString("avatar", null).apply();

        startActivity(new Intent(LoginActivity.this, MainRedirectionActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        clearApp();
    }

    public void onClick(View view) {
        if ((Objects.requireNonNull(binding.getLoginViewModel().password.get()).length() > 0) && (Objects.requireNonNull(binding.getLoginViewModel().cpf.get()).length() > 13)) {
            binding.getLoginViewModel().onClick(deviceId);
            if (binding.switch1.isChecked()) {
                MyApplication.preferences.edit().putBoolean("loginBiometric", true).apply();
            } else {
                MyApplication.preferences.edit().putBoolean("loginBiometric", false).apply();
            }
        }
        validateFields();
    }

    private void validateFields() {
        String nome = binding.editTextCPF.getText().toString();
        String sobrenome = binding.editTextPassword.getText().toString();

        if (isEmptyField(nome)) {
            if (isEmptyField(sobrenome)) {
                Toast.makeText(this, "Favor informar CPF e senha", Toast.LENGTH_LONG).show();
                findViewById(R.id.editTextCPF).requestFocus();
            } else {
                Toast.makeText(this, "Favor informar CPF", Toast.LENGTH_LONG).show();
                findViewById(R.id.editTextCPF).requestFocus();
            }

        } else if (isEmptyField(sobrenome)) {
            Toast.makeText(this, "Favor informar a senha", Toast.LENGTH_LONG).show();
            findViewById(R.id.editTextPassword).requestFocus();
        }
    }

    private boolean isEmptyField(String input) {
        boolean result = (TextUtils.isEmpty(input) || input.trim().isEmpty());
        return result;
    }

    public void onClickForgotPassword(View view) {
        registerViewModel.generateToken(Constants.BEARER_API, MyApplication.preferences.getString("cpf", null), "FORGOT_PASSWORD");
        Intent intent = new Intent(LoginActivity.this, OuzeSmsConfirmationActivity.class);
        intent.putExtra("isForgot", true)
                .putExtra("cpf", MyApplication.preferences.getString("cpf", null));
        startActivity(intent);
    }

    public void bindLoadingState() {
        binding.getLoginViewModel().loginBiometric.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean loginBiometric = binding.getLoginViewModel().loginBiometric.get();
                if (loginBiometric) {
                    new BiometricManager.BiometricBuilder(LoginActivity.this)
                            .setTitle("Posicione o dedo no leitor de digitais")
                            .setNegativeButtonText("Digitar senha")
                            .build()
                            .authenticate(LoginActivity.this);
                }
            }
        });
        binding.getLoginViewModel().firstLogin.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean firstLogin = binding.getLoginViewModel().firstLogin.get();
                if (firstLogin) {
                    if (binding.switch1.isChecked()) {
                        initBiometricLogin();
                    } else {
                        MyApplication.preferences.edit().putBoolean("isRegister", true).apply();
                        MyApplication.preferences.edit().putBoolean("newSession", true).apply();
                        startService(new Intent(LoginActivity.this, LogoutService.class));
                        startActivity(
                                new Intent(
                                        LoginActivity.this, HomeActivity.class));
                        finish();
                    }
                }
            }
        });
        binding.getLoginViewModel().succesfulLogin.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int i) {
                boolean successLogin = binding.getLoginViewModel().succesfulLogin.get();
                if (!successLogin) {
                    openDialog(binding.getLoginViewModel().errorMessage.get());
                }

            }
        });

        binding.getLoginViewModel().loadingState.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        boolean isLoading = binding.getLoginViewModel().loadingState.get();
                        if (isLoading) {
                            progressDialog =
                                    ProgressDialog.show(LoginActivity.this, "Entrando",
                                            "Aguarde...", true, false);
                        } else {
                            binding.getLoginViewModel().password.set("");
                            progressDialog.dismiss();

                        }
                    }
                });

        binding.getLoginViewModel().isLoginDone.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        boolean isLoginDone = binding.getLoginViewModel().isLoginDone.get();

                        if (isLoginDone) {
                            MyApplication.preferences.edit().putBoolean("isRegister", true).apply();
                            MyApplication.preferences.edit().putBoolean("newSession", true).apply();
                            startService(new Intent(LoginActivity.this, LogoutService.class));
                            startActivity(
                                    new Intent(
                                            LoginActivity.this, HomeActivity.class));
                            finish();
                        }
                    }
                });

    }

    public void openDialog(String text) {
        LayoutInflater li = getLayoutInflater();
        View v = li.inflate(R.layout.modal_info, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(v);
        TextView tvTitle = v.findViewById(R.id.tvtitle);
        tvTitle.setText("Erro ao logar");
        TextView tv = v.findViewById(R.id.tvDescribe);
        tv.setText(text);
        ImageView t = v.findViewById(R.id.imgClose);
        t.setOnClickListener(v1 -> {
            alert.dismiss();
        });
        alert = builder.create();
        alert.show();
    }

    private void initBiometricLogin() {
        new BiometricManager.BiometricBuilder(LoginActivity.this)
                .setTitle("Posicione o dedo no leitor de digitais")
                .setNegativeButtonText("Digitar senha")
                .build()
                .authenticate(LoginActivity.this);
    }

    @Override
    public void onSdkVersionNotSupported() {
        Toast.makeText(getApplicationContext(), "Versão do android não suportada", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        Toast.makeText(getApplicationContext(), "Aparelho não suporta Biometria", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        Toast.makeText(getApplicationContext(), "Biometria não disponível", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(getApplicationContext(), "Aparelho sem permissões para biometria", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {

    }

    @Override
    public void onAuthenticationFailed() {
    }

    @Override
    public void onAuthenticationCancelled() {
        Toast.makeText(getApplicationContext(), "Biometria cancelada", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationSuccessful() {
        binding.editTextCPF.setText(MyApplication.preferences.getString("cpf", null));
        binding.editTextPassword.setText(MyApplication.preferences.getString("version", null));
        binding.getLoginViewModel().onClick(deviceId);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
//        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.parseColor("#FF2D2553"));
        }
    }
}
