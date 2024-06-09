package com.hynguyen.chitieucanhan.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.adapter.ViTienAdapter;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.fragment.ThemDanhMucFragment;
import com.hynguyen.chitieucanhan.fragment.ThemViTienFragment;
import com.hynguyen.chitieucanhan.mdel.ViTien;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class ViTienActivity extends AppCompatActivity {
    private MaterialToolbar tbSetting;
    private RecyclerView rvViTien;
    private ViTienAdapter viTienAdapter;
    private AppViewModel appViewModel;
    private LiveData<List<ViTien>> listViTien;
    private ViTien viTienChon;
    private int soLuongViTien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_tien);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        //Toolbar
        tbSetting = findViewById(R.id.tbViTien);
        tbSetting.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tbSetting.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showBottomSheetThemViTien();
                return true;
            }
        });

        //Đổ dữ liệu ví tiền vào RecyclerView
        rvViTien = findViewById(R.id.rvViTien);
        rvViTien.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viTienAdapter = new ViTienAdapter(this);
        rvViTien.setAdapter(viTienAdapter);
        listViTien = appViewModel.tatCaViTien();
        listViTien.observe(this, new Observer<List<ViTien>>() {
            @Override
            public void onChanged(List<ViTien> listViTien) {
                viTienAdapter.submitList(listViTien);
                soLuongViTien = listViTien.size();
            }
        });
        viTienAdapter.setOnItemClickListener(new ViTienAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViTien viTien) {
                viTienChon = viTien;
                showBottomSheetXoaSua();
            }
        });
    }

    public void showBottomSheetThemViTien() {
        ThemViTienFragment themViTienFragment = ThemViTienFragment.newInstance();
        themViTienFragment.show(getSupportFragmentManager(), ThemViTienFragment.TAG);
    }

    //Hiển thị menu Xóa/Chỉnh sửa ví tiền
    private void showBottomSheetXoaSua() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_menu_xoa_sua);
        bottomSheetDialog.show();
        Button btnXoa = bottomSheetDialog.findViewById(R.id.btnXoa);
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soLuongViTien>1) {
                    if (viTienChon != null) {
                        appViewModel.xoaViTien(viTienChon);
                        bottomSheetDialog.dismiss();
                        Toasty.success(getApplicationContext(), "Xóa ví tiền thành công!", Toasty.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(getApplicationContext(), "Lỗi, hãy thử lại!", Toasty.LENGTH_SHORT, true).show();
                        bottomSheetDialog.dismiss();
                    }
                } else {
                    Toasty.error(getApplicationContext(),"Phải có ít nhất 1 ví tiền",Toasty.LENGTH_SHORT,true).show();
                }
            }
        });
        Button btnChinhSua = bottomSheetDialog.findViewById(R.id.btnChinhSua);
        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemViTienFragment themViTienFragment = ThemViTienFragment.newInstance(viTienChon);
                themViTienFragment.show(getSupportFragmentManager(), ThemViTienFragment.TAG);
                bottomSheetDialog.dismiss();
            }
        });
    }
}