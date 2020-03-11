package br.com.calcard.android.app.ui.travel_notification;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.databinding.ActivityConfirmTravelNotificationBinding;

public class ConfirmTravelNotification extends AppCompatActivity {
    ConfirmTravelNotificationViewModel viewModel;
    ActivityConfirmTravelNotificationBinding binding;
    Calendar myCalendar = Calendar.getInstance();
    Calendar cal = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateInitial;
    DatePickerDialog.OnDateSetListener dateFinal;
    List<String> listCountries;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_travel_notification);
        viewModel= new ConfirmTravelNotificationViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_travel_notification);
        binding.setViewModel(viewModel);
        setStatusBarGradiant(this);
        binding.executePendingBindings();
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        listCountries = getIntent().getStringArrayListExtra("countries");
        binding.tvContinente.setText(getIntent().getStringExtra("name"));
        binds();

        binding.editText2.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                setDateInitial();
            }
        });
        binding.editText3.setOnFocusChangeListener((v, hasFocus) ->{
            if (hasFocus){
                setDateFinal();
            }
        });

        dateInitial = (view, year, monthOfYear, dayOfMonth) -> {
            binding.getViewModel().initialDate.set(String.format("%02d",dayOfMonth)+"/"+String.format("%02d",monthOfYear+1)+"/"+String.valueOf(year));
            binding.editText2.clearFocus();
            cal.set(year,monthOfYear,dayOfMonth);
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        };
        dateFinal = (view, year, monthOfYear, dayOfMonth) -> {
            binding.getViewModel().finalDate.set(String.format("%02d",dayOfMonth)+"/"+String.format("%02d",monthOfYear+1)+"/"+String.valueOf(year));
            binding.editText3.clearFocus();
            binding.button19.setBackgroundResource(R.drawable.btn_rouded_orange);
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        };

    }

    public void setDateInitial(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(ConfirmTravelNotification.this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        DatePickerDialog dialog =new DatePickerDialog(ConfirmTravelNotification.this,R.style.DialogTheme, dateInitial, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        dialog.show();

    }
    public void setDateFinal(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(ConfirmTravelNotification.this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        DatePickerDialog dialog =new DatePickerDialog(ConfirmTravelNotification.this,R.style.DialogTheme, dateFinal, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        dialog.show();
    }

    public void binds(){
        binding.getViewModel().progress.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean progress= binding.getViewModel().progress.get();
                if (progress){
                    progressDialog =
                            ProgressDialog.show(ConfirmTravelNotification.this,"Salvando Dados",
                                    "Aguarde...",true, false);
                }else {
                    progressDialog.dismiss();
                }
            }
        });

        binding.getViewModel().listOk.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (binding.getViewModel().listOk.get().equals("ok")){
                    Intent intent = new Intent(ConfirmTravelNotification.this, SuccessTravelNotificationActivity.class);
                    intent.putExtra("continent",binding.tvContinente.getText());
                    intent.putExtra("initDate", binding.getViewModel().initialDate.get());
                    intent.putExtra("finalDate",binding.getViewModel().finalDate.get());
                    intent.putStringArrayListExtra("countries", (ArrayList<String>) listCountries);
                    startActivity(intent);
                    finish();
                }else if (!binding.getViewModel().listOk.get().equals("")){
                    Toast.makeText(ConfirmTravelNotification.this,binding.getViewModel().listOk.get(),Toast.LENGTH_SHORT).show();
                }
                binding.getViewModel().listOk.set("");
            }
        });
    }

    public void onClickNext(View view){
        String dataInicial = binding.getViewModel().getDate(binding.getViewModel().initialDate.get());
        String dataFinal = binding.getViewModel().getDate(binding.getViewModel().finalDate.get());
        binding.getViewModel().postNotification(dataInicial,dataFinal,listCountries);

    }



    public void onClickChange(View view){
        super.onBackPressed();
        finish();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.parseColor("#2D2553"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
