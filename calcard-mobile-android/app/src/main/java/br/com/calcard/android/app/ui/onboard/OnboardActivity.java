package br.com.calcard.android.app.ui.onboard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.Vector;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.adapter.PagerAdapter;
import br.com.calcard.android.app.databinding.ActivityOnboardBinding;
import br.com.calcard.android.app.ui.fragments.Onboard0;
import br.com.calcard.android.app.ui.fragments.Onboard1;
import br.com.calcard.android.app.ui.fragments.Onboard2;
import br.com.calcard.android.app.ui.redirection.MainRedirectionActivity;

public class OnboardActivity extends AppCompatActivity {

    private OnboardViewModel onboardViewModel;
    private ActivityOnboardBinding binding;
    private int dotsCount = 3;    //No of tabs or images
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onboardViewModel = new OnboardViewModel();
        setContentView(R.layout.activity_onboard);
        setStatusBarGradiant(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboard);
        binding.setOnboardViewModel(onboardViewModel);
        binding.executePendingBindings();
        initialisePaging();
    }

    @Override
    public void onBackPressed() {

    }

    public void migrationOnboard(View view) {
        Intent intent = new Intent(this, MainRedirectionActivity.class);
        startActivity(intent);
        finish();
    }

    private void initialisePaging() {

        List<Fragment> fragments = new Vector<>();

        fragments.add(Fragment.instantiate(this, Onboard0.class.getName()));
        fragments.add(Fragment.instantiate(this, Onboard1.class.getName()));
        fragments.add(Fragment.instantiate(this, Onboard2.class.getName()));

        PagerAdapter mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        binding.viewPager.setAdapter(mPagerAdapter);
        drawPageSelectionIndicators(0);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                drawPageSelectionIndicators(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        binding.getOnboardViewModel().setFirstRun();
    }

    private void drawPageSelectionIndicators(int mPosition) {
        if (binding.viewPagerCountDots != null) {
            binding.viewPagerCountDots.removeAllViews();
        }
        dots = new ImageView[3];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            if (i == mPosition)
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_selected));
            else
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.item_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(8, 0, 8, 0);
            binding.viewPagerCountDots.addView(dots[i], params);
        }
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