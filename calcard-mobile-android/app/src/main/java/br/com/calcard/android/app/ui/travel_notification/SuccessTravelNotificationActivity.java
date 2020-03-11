package br.com.calcard.android.app.ui.travel_notification;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.List;
import java.util.Objects;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.databinding.ActivitySuccessTravelNotificationBinding;
import br.com.calcard.android.app.ui.cards.HomeActivity;

public class SuccessTravelNotificationActivity extends AppCompatActivity {

    SuccessTravelNotificationViewModel viewModel;
    ActivitySuccessTravelNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_travel_notification);
        viewModel= new SuccessTravelNotificationViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_success_travel_notification);
        setStatusBarGradiant(this);
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        List<String> countries= getIntent().getStringArrayListExtra("countries");
        binding.textView75.setText(getIntent().getStringExtra("continent"));
        binding.textView80.setText(getIntent().getStringExtra("initDate")+ " a "+getIntent().getStringExtra("finalDate"));
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<countries.size();i++){
            sb.append(countries.get(i));
            if (countries.size()!=i+1){
                sb.append(", ");
            }
        }
        binding.textView78.setText(sb);
    }
    public void onClickInit(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
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
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
