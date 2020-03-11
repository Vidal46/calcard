package br.com.calcard.android.app.ui.limitrequest;

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
import androidx.databinding.Observable;

import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.R;
import br.com.calcard.android.app.databinding.ActivityLimitRequestBinding;
import br.com.calcard.android.app.ui.cards.HomeActivity;
import br.com.calcard.android.app.utils.Constants;

public class LimitRequestActivity extends AppCompatActivity {
    LimitRequestViewModel viewModel;
    ActivityLimitRequestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit_request);
        setStatusBarGradiant(this);
        viewModel= new LimitRequestViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_limit_request);
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
        setSupportActionBar(binding.toolbar);
        binds();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        binding.getViewModel().getLimits(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser",null));

    }

    public void binds(){
        binding.getViewModel().total.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.progressBar6.setMax((int)Math.round(binding.getViewModel().total.get()));
                binding.progressBar6.setProgress((int)Math.round(binding.getViewModel().valueProgress.get()));
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(LimitRequestActivity.this, HomeActivity.class));
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
