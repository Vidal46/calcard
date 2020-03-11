package br.com.calcard.android.app.ui.changepasswordcard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import java.util.Objects;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.databinding.ActivityChangePasswordCardBinding;
import br.com.calcard.android.app.ui.cards.HomeActivity;

public class ChangePasswordCard extends AppCompatActivity {
    private ActivityChangePasswordCardBinding binding;
    AlertDialog alert;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_card);
        setStatusBarGradiant(this);
        ChangePasswordCardViewModel viewModel = new ChangePasswordCardViewModel();
        binding= DataBindingUtil.setContentView(this,R.layout.activity_change_password_card);
        binding.setVielModel(viewModel);
        binding.executePendingBindings();
        setSupportActionBar(binding.toolbar3);
        bind();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.layout.clearFocus();
        binding.layout.setOnClickListener(v -> binding.layout.requestFocus());
        binding.tvWhat.setOnClickListener(view -> {
            LayoutInflater li = getLayoutInflater();
            View v = li.inflate(R.layout.modal_unlock_card, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordCard.this);
            builder.setView(v);
            TextView t = v.findViewById(R.id.tvOK);
            t.setOnClickListener(v1 -> {
                alert.dismiss();
            });
            alert = builder.create();
            alert.show();
        });
        binding.toolbarTitle.setText("Senha do cartão");
        binding.edtCvv.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                binding.edtCvv.setHint("");
                binding.edtCvv.setCursorVisible(true);
            }else {
                binding.edtCvv.setHint(" - - -");
            }
        });
    }
    public boolean isValidCvv(){
        if (binding.edtCvv.length()==3){
            return true;
        }else {
            return false;
        }
    }

    public  boolean isValidLengthPassword(String password){
        if (password.length()==4){
            return true;
        }else return false;
    }
    public  boolean isMatchPasswords(){
        if (binding.edtnewPassword.getText().toString().equals(Objects.requireNonNull(binding.edtConfirmNewPassword.getText()).toString())){
            return true;
        }else return false;
    }

    public void onClick(View view){
        binding.textInputLayout8.setError(null);
        binding.inputlayoutConfirm.setError(null);
        binding.textInputLayout7.setError(null);
        binding.inputLayoutPassword.setError(null);

        if (!isValidCvv())binding.textInputLayout8.setError("Número de dígitos inválidos");
        else if (!isValidLengthPassword(binding.edtCurrentPassword.getText().toString()))binding.inputLayoutPassword.setError("Tamanho da senha deve ter 4 dígitos");
        else if (!isValidLengthPassword(binding.edtnewPassword.getText().toString()))binding.textInputLayout7.setError("Nova senha deve ter 4 dígitos");
        else if (!isMatchPasswords())binding.inputlayoutConfirm.setError("As senhas devem ser iguais");
//        else binding.getVielModel().changePassword();

    }

    public void bind(){
        binding.getVielModel().progress.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (binding.getVielModel().progress.get()){
                    progressDialog =
                            ProgressDialog.show(ChangePasswordCard.this,"Trocando a senha",
                                    "Aguarde...",true, false);
                }else {
                    progressDialog.dismiss();
                }
            }
        });
        binding.getVielModel().responseMessage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String message= binding.getVielModel().responseMessage.get();
                if (message.equals("ok")){
                    dialogSuccess();
                }else if (!message.equals("")){
                    dialogError(message);
                }
                binding.getVielModel().responseMessage.set("");
            }
        });
    }

    public void dialogError(String text){
        LayoutInflater li = getLayoutInflater();
        View v = li.inflate(R.layout.modal_info, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordCard.this);
        builder.setView(v);
        TextView tvTitle = v.findViewById(R.id.tvtitle);
        tvTitle.setText("Erro ao alterar senha");
        TextView tv = v.findViewById(R.id.tvDescribe);
        tv.setText(text);
        ImageView t = v.findViewById(R.id.imgClose);
        t.setOnClickListener(v1 -> {
            alert.dismiss();
        });
        alert = builder.create();
        alert.show();
    }

    public void dialogSuccess(){
        LayoutInflater li = getLayoutInflater();
        View v = li.inflate(R.layout.modal_success_password_change, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordCard.this);
        builder.setView(v);
        Button btn = v.findViewById(R.id.btnOk);
        btn.setOnClickListener(v1 -> {
            Intent intent = new Intent(ChangePasswordCard.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
        alert = builder.create();
        alert.show();
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
