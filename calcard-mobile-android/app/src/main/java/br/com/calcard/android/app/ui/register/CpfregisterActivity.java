package br.com.calcard.android.app.ui.register;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import br.com.calcard.android.app.databinding.ActivityCpfregisterBinding;
import br.com.calcard.android.app.ui.smsverification.SmsConfirmation;
import br.com.calcard.android.app.utils.Constants;
import br.com.calcard.android.app.utils.Mask;

public class CpfregisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;
    private ActivityCpfregisterBinding binding;
    private ProgressDialog progressDialog;
    boolean forgot = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpfregister);
        setStatusBarGradiant(this);
        registerViewModel = new RegisterViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cpfregister);
        binding.setRegisterViewModel(registerViewModel);
        binding.executePendingBindings();
        bindLoadingState();
        forgot = getIntent().getBooleanExtra("isForgot", false);
        if (forgot) {
            binding.textView6.setText("Insira seu cpf para realizarmos a troca de senha");
        }
        binding.editTextCPF.addTextChangedListener(Mask.insert("###.###.###-##", binding.editTextCPF));
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }

    public void onClick(View view) {

        if (binding.getRegisterViewModel().cpf.get().length() == 14) {
            if (binding.getRegisterViewModel().phone.get() != null) {
                binding.getRegisterViewModel().cpfOk.set("valido");
            } else if (forgot) {
                binding.getRegisterViewModel().generateToken(Constants.BEARER_API, null, "FORGOT_PASSWORD");
            } else {
                binding.getRegisterViewModel().generateToken(Constants.BEARER_API, null, "REGISTER");
            }

        } else {
            Toast.makeText(this, "formato errado", Toast.LENGTH_SHORT).show();
        }
    }

    public void bindLoadingState() {
        binding.getRegisterViewModel().loadingState.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable sender, int propertyId) {
                        if (binding.getRegisterViewModel().loadingState.get()) {
                            progressDialog =
                                    ProgressDialog.show(CpfregisterActivity.this, "Validando",
                                            "Aguarde...", true, false);
                        } else {
                            progressDialog.dismiss();

                        }
                    }
                });
        binding.getRegisterViewModel().cpfOk.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String cpfOk = binding.getRegisterViewModel().cpfOk.get();
                if (cpfOk.equals("valido")) {
                    Intent intent = new Intent(CpfregisterActivity.this, SmsConfirmation.class);
                    intent.putExtra("cpf", binding.getRegisterViewModel().cpf.get());
                    String phone = (binding.getRegisterViewModel().phone.get());
                    intent.putExtra("phone", phone);
                    intent.putExtra("isForgot", forgot);
                    startActivity(intent);
                    finish();
                } else if (!cpfOk.equals("")) {
                    if (binding.getRegisterViewModel().phone.get() == null) {
                        Toast.makeText(CpfregisterActivity.this, cpfOk, Toast.LENGTH_LONG).show();
                    }
                }
                binding.getRegisterViewModel().cpfOk.set("");
            }
        });
        binding.getRegisterViewModel().cpf.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.getRegisterViewModel().phone.set(null);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.parseColor("#2D2553"));
        }
    }

}
