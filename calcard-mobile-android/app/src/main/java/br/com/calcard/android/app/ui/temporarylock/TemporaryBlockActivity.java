package br.com.calcard.android.app.ui.temporarylock;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.R;
import br.com.calcard.android.app.databinding.ActivityTemporaryBlockBinding;
import br.com.calcard.android.app.ui.cards.HomeActivity;
import br.com.calcard.android.app.utils.Constants;

public class TemporaryBlockActivity extends AppCompatActivity {
    TemporaryBlockViewModel viewModel;
    private ActivityTemporaryBlockBinding binding;
    AlertDialog alert;
    private ProgressDialog progressDialog;
    private boolean neverUnlocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary_block);
        setStatusBarGradiant(this);
        viewModel = new TemporaryBlockViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_temporary_block);
        binding.setViewUnlock(viewModel);
        binding.executePendingBindings();
        setSupportActionBar(binding.toolbar3);
        String status = MyApplication.preferences.getString("statusCard", "NULL");
        neverUnlocked = MyApplication.preferences.getBoolean("neverUnlocked", true);
        if (status.contains("BLOQUEADO") || status.contains("CANCELADO")) {
            binding.layoutConfirm.setVisibility(View.VISIBLE);
            if (status.equalsIgnoreCase("BLOQUEADO")) {
                binding.textView10.setText("Seu cartão já encontra-se bloqueado!");
            } else {
                binding.textView10.setText("Seu cartão encontra-se cancelado");
            }
            binding.layoutPrincipal.setVisibility(View.GONE);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.toolbarTitle.setText("Bloquear cartão");
        binding.edtCvv.requestFocus();
        binds();
        binding.button8.setEnabled(true);
        binding.button8.setBackgroundResource(R.drawable.btn_rouded_orange);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        binding.edtCvv.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayout8.setError(null);
                binding.edtCvv.setHint("");
                binding.edtCvv.setCursorVisible(true);
            } else {
                if (!isValidCvv()) {
                    binding.textInputLayout8.setError("Formato do cvv incorreto");
                }
            }
        });
        binding.edtnewPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayout7.setError(null);
            } else {
                if (!isValidLengthPassword(binding.edtnewPassword.getText().toString())) {
                    binding.textInputLayout7.setError("Senha deve conter 4 dígitos");
                }
            }
        });
        binding.edtConfirmNewPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.inputlayoutConfirm.setError(null);
            } else {
                if (!isMatchPasswords()) {
                    binding.inputlayoutConfirm.setError("Senhas não conferem");
                }
            }
        });

        binding.edtConfirmNewPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_FLAG_NO_ENTER_ACTION) {
                binding.inputlayoutConfirm.clearFocus();
                if (binding.textInputLayout8.getError() == null && binding.textInputLayout7.getError() == null && binding.inputlayoutConfirm.getError() == null) {
                    binding.button8.setEnabled(true);
                    binding.button8.setBackgroundResource(R.drawable.btn_rouded_orange);


                }
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }

            return false;
        });
        binding.edtConfirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==4){
                    binding.inputlayoutConfirm.clearFocus();
                    if (binding.textInputLayout8.getError() == null && binding.textInputLayout7.getError() == null && binding.inputlayoutConfirm.getError() == null) {
                        binding.button8.setEnabled(true);
                        binding.button8.setBackgroundResource(R.drawable.btn_rouded_orange);
                    }
                    View view = getWindow().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                }

            }
        });


    }

    public void onClickText(View view) {
        LayoutInflater li = getLayoutInflater();
        View v = li.inflate(R.layout.modal_unlock_card, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(TemporaryBlockActivity.this);
        builder.setView(v);
        TextView t = v.findViewById(R.id.tvOK);
        t.setOnClickListener(v1 -> {
            alert.dismiss();
        });
        alert = builder.create();
        alert.show();

    }

    public void binds() {
        binding.getViewUnlock().isCvvUnlocked.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (binding.getViewUnlock().isCvvUnlocked.get().equals("locked")) {
                    binding.layoutConfirm.setVisibility(View.VISIBLE);
                    binding.layoutPrincipal.setVisibility(View.GONE);

                } else if (binding.getViewUnlock().isCvvUnlocked.get().equals("error")) {
                    Toast.makeText(TemporaryBlockActivity.this, "Código de verificação do cartão incorreto.", Toast.LENGTH_SHORT).show();
                }
                binding.getViewUnlock().isCvvUnlocked.set("");
            }
        });
        binding.getViewUnlock().isLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (binding.getViewUnlock().isLoading.get()) {
                    progressDialog =
                            ProgressDialog.show(TemporaryBlockActivity.this, "Bloqueando o cartão",
                                    "Aguarde...", true, false);
                } else {
                    progressDialog.dismiss();

                }
            }
        });
    }

    public void onClick(View view) {
        if(!neverUnlocked) {
            if (binding.edtCvv.length() == 3) {
                binding.getViewUnlock().lock(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), MyApplication.preferences.getString("card", null));
            } else {
                Toast.makeText(TemporaryBlockActivity.this, "O campo deve conter 3 dígitos", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(TemporaryBlockActivity.this, "Cartão não pode ser bloqeado, menu indisponível", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickConfirm(View view) {
        startActivity(new Intent(TemporaryBlockActivity.this, HomeActivity.class));
    }

    public boolean isValidCvv() {
        if (binding.edtCvv.length() == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isValidLengthPassword(String password) {
        if (password.length() == 4) {
            return true;
        } else return false;
    }

    public boolean isMatchPasswords() {
        if (binding.edtnewPassword.getText().toString().equals(Objects.requireNonNull(binding.edtConfirmNewPassword.getText()).toString())) {
            return true;
        } else return false;
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
