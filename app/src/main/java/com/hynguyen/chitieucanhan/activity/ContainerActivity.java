package com.hynguyen.chitieucanhan.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.adapter.ViewPagerAdapter;
import com.hynguyen.chitieucanhan.fragment.CaiDatFragment;
import com.hynguyen.chitieucanhan.fragment.ChiTieuFragment;
import com.hynguyen.chitieucanhan.fragment.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContainerActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager2 vpContaner;
    private BottomNavigationView bnvContainer;
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList;
    private TextView txtTBTitle;
    private ImageButton btnThemThuChi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        addView();
    }

    private void addView() {
        txtTBTitle = findViewById(R.id.txtTBTitle);
        vpContaner = findViewById(R.id.vpContaner);
        bnvContainer = findViewById(R.id.bnvContainer);
        xuLyViewPager();

        btnThemThuChi = findViewById(R.id.btnThemThuChi);
        btnThemThuChi.setOnClickListener(this);
    }

    private void xuLyViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new ChiTieuFragment());
        fragmentList.add(new CaiDatFragment());
        viewPagerAdapter = new ViewPagerAdapter(this, fragmentList);
        vpContaner.setAdapter(viewPagerAdapter);
        vpContaner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bnvContainer.getMenu().findItem(R.id.menuHome).setChecked(true);
                        txtTBTitle.setText("Báo cáo");
                        showButtonThemThuChi();
                        break;
                    case 1:
                        bnvContainer.getMenu().findItem(R.id.menuThuNhap).setChecked(true);
                        txtTBTitle.setText("Thu chi");
                        showButtonThemThuChi();
                        break;
                    case 2:
                        bnvContainer.getMenu().findItem(R.id.menuCaiDat).setChecked(true);
                        txtTBTitle.setText("Cài đặt");
                        hideButtonThemThuChi();
                        break;
                }
            }
        });
        bnvContainer.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuHome:
                        vpContaner.setCurrentItem(0);
                        txtTBTitle.setText("Báo cáo");
                        showButtonThemThuChi();
                        break;
                    case R.id.menuThuNhap:
                        vpContaner.setCurrentItem(1);
                        txtTBTitle.setText("Thu chi");
                        showButtonThemThuChi();
                        break;
                    case R.id.menuCaiDat:
                        vpContaner.setCurrentItem(2);
                        txtTBTitle.setText("Cài đặt");
                        hideButtonThemThuChi();
                        break;
                }
                return true;
            }
        });
    }

    private void showButtonThemThuChi() {
        // Precondition
        if (btnThemThuChi.getVisibility() == View.VISIBLE) {
            return;
        }

        // Animate the hidden linear layout as visible and set
        // the alpha as 0.0. Otherwise the animation won't be shown
        btnThemThuChi.setVisibility(View.VISIBLE);
        btnThemThuChi.setAlpha(0.0f);
        btnThemThuChi
                .animate()
                .setDuration(150)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        btnThemThuChi.animate().setListener(null);
                    }
                })
        ;
    }

    private void hideButtonThemThuChi() {
        // Precondition
        if (btnThemThuChi.getVisibility() != View.VISIBLE) {
            return;
        }

        // Animate the hidden linear layout as visible and set
        btnThemThuChi
                .animate()
                .setDuration(150)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        btnThemThuChi.setVisibility(View.GONE);
                        // Hack: Remove the listener. So it won't be executed when
                        // any other animation on this view is executed
                        btnThemThuChi.animate().setListener(null);
                    }
                })
        ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnThemThuChi:
                startActivity(new Intent(getApplicationContext(),ThemChiTieuActivity.class));
                break;
        }
    }
}