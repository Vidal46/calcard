package br.com.calcard.android.app.ui.travel_notification;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Objects;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.adapter.CountriesAdapter;
import br.com.calcard.android.app.databinding.ActivityChooseCountryBinding;
import br.com.calcard.android.app.utils.RecyclerItemClickListener;

public class ChooseCountryActivity extends AppCompatActivity {

    ChooseCountryViewModel viewModel;
    ActivityChooseCountryBinding binding;
    ArrayList<String> countriesSelected= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_country);
        viewModel= new ChooseCountryViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_country);
        binding.setViewModel(viewModel);
        setStatusBarGradiant(this);
        binding.executePendingBindings();
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        binding.tvContinente.setText(getIntent().getStringExtra("name"));
        binding.getViewModel().getCountries(getIntent().getStringExtra("code"));
        binds();

    }

    public void binds(){
        binding.getViewModel().listOk.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String message= binding.getViewModel().listOk.get();
                if (message.equals("ok")){
                    CountriesAdapter adapter = new CountriesAdapter(binding.getViewModel().countries, ChooseCountryActivity.this);
                    binding.rv.setLayoutManager(new LinearLayoutManager(ChooseCountryActivity.this));
                    binding.rv.setAdapter(adapter);
                    binding.progress.setVisibility(View.GONE);
                    binding.rv.addOnItemTouchListener(new RecyclerItemClickListener(ChooseCountryActivity.this, binding.rv, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            binding.getViewModel().countries.get(position).setSelected(!binding.getViewModel().countries.get(position).isSelected());
                            ImageView img = view.findViewById(R.id.imageView19);
                            TextView countrytext = view.findViewById(R.id.textView71);
                            img.setVisibility(binding.getViewModel().countries.get(position).isSelected()? View.VISIBLE:View.INVISIBLE);
                            if (binding.getViewModel().countries.get(position).isSelected()){
                                countrytext.setTypeface(null,Typeface.BOLD);
                            }else {
                                countrytext.setTypeface(null,Typeface.NORMAL);

                            }
                            if (binding.getViewModel().countries.get(position).isSelected()){
                                countriesSelected.add(binding.getViewModel().countries.get(position).getName());
                            }else {
                                for (int i=0;i<countriesSelected.size();i++){
                                    if (binding.getViewModel().countries.get(position).getName().equals(countriesSelected.get(i))){
                                        countriesSelected.remove(i);
                                    }
                                }
                            }
                            if (countriesSelected.size()>0){
                                binding.button18.setEnabled(true);
                                binding.button18.setBackgroundResource(R.drawable.btn_rouded_orange);
                            }else {
                                binding.button18.setEnabled(false);
                                binding.button18.setBackgroundResource(R.drawable.btn_disable);
                            }
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    }));

                }else if (!message.equals("")){
                    Toast.makeText(ChooseCountryActivity.this,message,Toast.LENGTH_LONG ).show();
                    binding.progress.setVisibility(View.GONE);

                }
                binding.getViewModel().listOk.set("");
            }
        });
    }
    public void OnClickNext(View view){
        Intent intent = new Intent(ChooseCountryActivity.this,ConfirmTravelNotification.class);
        intent.putStringArrayListExtra("countries",countriesSelected);
        intent.putExtra("name",binding.tvContinente.getText());
        startActivity(intent);
        finish();
    }

    public void onClickChange(View view){
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
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
