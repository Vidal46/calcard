
package br.com.calcard.android.app.ui.secure;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Objects;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.adapter.SecureListAdapter;
import br.com.calcard.android.app.databinding.ActivitySecureListBinding;

public class SecureListActivity extends AppCompatActivity {
    SecureListViewModel viewModel;
    ActivitySecureListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_list);
        viewModel= new SecureListViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_secure_list);
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
        binds();
        setSupportActionBar(binding.toolbar4);
        binding.getViewModel().getInsurances();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void binds(){
        binding.getViewModel().listOk.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String message= binding.getViewModel().listOk.get();
                if (message.equals("ok")){
                    SecureListAdapter adapter = new SecureListAdapter(binding.getViewModel().insurances,SecureListActivity.this);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(SecureListActivity.this));
                    binding.recyclerView.setAdapter(adapter);
                    binding.progress.setVisibility(View.GONE);

                }else if (!message.equals("")){
                    Toast.makeText(SecureListActivity.this,message,Toast.LENGTH_LONG ).show();
                    binding.progress.setVisibility(View.GONE);

                }
                binding.getViewModel().listOk.set("");
            }
        });
    }
}
