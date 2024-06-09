package com.hynguyen.chitieucanhan.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.fragment.DanhMucChiFragment;
import com.hynguyen.chitieucanhan.fragment.DanhMucThuFragment;
import com.hynguyen.chitieucanhan.fragment.ThemDanhMucFragment;

public class DanhMucActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private MaterialToolbar tbDanhMuc;
    private MaterialButtonToggleGroup tgbDanhMuc;
    private MaterialButton btnDanhMucThu, btnDanhMucChi;
    private FrameLayout flDanhMuc;
    private DanhMucThuFragment danhMucThuFragment;
    private DanhMucChiFragment danhMucChiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc);

        tbDanhMuc = findViewById(R.id.tbDanhMuc);
        tbDanhMuc.setOnMenuItemClickListener(this);
        danhMucThuFragment = new DanhMucThuFragment();
        danhMucChiFragment = new DanhMucChiFragment();
        tbDanhMuc.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tgbDanhMuc = findViewById(R.id.tgbDanhMuc);
        tgbDanhMuc.check(R.id.btnDanhMucThu);
        fragmentTransaction(danhMucThuFragment);
        tgbDanhMuc.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch (checkedId) {
                    case R.id.btnDanhMucChi:
                        buttonDanhMucChi();
                        break;
                    case R.id.btnDanhMucThu:
                        buttonDanhMucThu();
                        break;
                }
            }
        });
        btnDanhMucThu = findViewById(R.id.btnDanhMucThu);
        btnDanhMucChi = findViewById(R.id.btnDanhMucChi);
        flDanhMuc = findViewById(R.id.flDanhMuc);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuThem:
                xuLyThemDanhMuc();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuThem:
            showBottomSheetThemDanhMuc();
                break;
        }
        return true;
    }

    private void xuLyThemDanhMuc() {

    }
    public void showBottomSheetThemDanhMuc() {
        ThemDanhMucFragment addPhotoBottomDialogFragment =
                ThemDanhMucFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                ThemDanhMucFragment.TAG);
    }
    private void buttonDanhMucThu() {
        btnDanhMucThu.setBackgroundColor(getResources().getColor(R.color.button_checked));
        btnDanhMucThu.setTextColor(getResources().getColor(R.color.white));
        btnDanhMucChi.setBackgroundColor(getResources().getColor(R.color.button_uncheck));
        btnDanhMucChi.setTextColor(getResources().getColor(R.color.black));
        fragmentTransaction(danhMucThuFragment);
    }

    private void buttonDanhMucChi() {
        btnDanhMucChi.setBackgroundColor(getResources().getColor(R.color.button_checked));
        btnDanhMucChi.setTextColor(getResources().getColor(R.color.white));
        btnDanhMucThu.setBackgroundColor(getResources().getColor(R.color.button_uncheck));
        btnDanhMucThu.setTextColor(getResources().getColor(R.color.black));
        fragmentTransaction(danhMucChiFragment);
    }

    private void fragmentTransaction(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (getSupportFragmentManager().findFragmentById(R.id.flDanhMuc) != null) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        fragmentTransaction.replace(R.id.flDanhMuc, fragment, null);
        fragmentTransaction.commit();
    }
}