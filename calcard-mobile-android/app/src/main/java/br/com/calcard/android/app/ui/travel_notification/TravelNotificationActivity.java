package br.com.calcard.android.app.ui.travel_notification;

import android.annotation.TargetApi;
import android.app.Activity;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Objects;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.adapter.ContinentsAdapter;
import br.com.calcard.android.app.databinding.ActivityTravelNotificationBinding;

public class TravelNotificationActivity extends AppCompatActivity {

    TravelNotificationViewModel viewModel;
    ActivityTravelNotificationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_notification);
        viewModel= new TravelNotificationViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_travel_notification);
        setStatusBarGradiant(this);
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        binds();
        binding.getViewModel().getContinents();

    }

    public void binds(){
        binding.getViewModel().listOk.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String message= binding.getViewModel().listOk.get();
                if (message.equals("ok")){
                    ContinentsAdapter adapter = new ContinentsAdapter(binding.getViewModel().continents, TravelNotificationActivity.this);
                    binding.rv.setLayoutManager(new LinearLayoutManager(TravelNotificationActivity.this));
                    binding.rv.setAdapter(adapter);
                    binding.progress.setVisibility(View.GONE);

                }else if (!message.equals("")){
                    Toast.makeText(TravelNotificationActivity.this,message,Toast.LENGTH_LONG ).show();
                    binding.progress.setVisibility(View.GONE);

                }
                binding.getViewModel().listOk.set("");
            }
        });
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
        }
        return super.onOptionsItemSelected(item);
    }
}
