package com.hynguyen.chitieucanhan.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.adapter.HinhDanhMucAdapter;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ThemDanhMucFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG = "ThemDanhMuc";
    private AppViewModel appViewModel;
    private MaterialButtonToggleGroup tgbLoaiDanhMuc;
    private MaterialButton btnDanhMucThu, btnDanhMucChi, btnHuyBo, btnHoanThanh;
    private ImageView imgChonHinh;
    private EditText txtTenDanhMuc;
    private ExpandableLayout expanHinhDanhMuc;
    private RecyclerView rvHinhDanhMuc;
    private HinhDanhMucAdapter hinhDanhMucAdapter;
    private List<String> listHinh;

    private int idDanhMuc;
    private int loaiDanhMuc;
    private String tenDanhMuc;
    private String hinhDanhMuc;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    public static ThemDanhMucFragment newInstance() {
        return new ThemDanhMucFragment();
    }

    //Lấy thông tin danh mục cần cập nhật
    public static ThemDanhMucFragment newInstance(DanhMuc danhMuc) {
        ThemDanhMucFragment dialog = new ThemDanhMucFragment();
        Bundle args = new Bundle();
        args.putInt("id", danhMuc.getId());
        args.putString("name", danhMuc.getTenDanhMuc());
        args.putString("img", danhMuc.getHinhAnh());
        args.putInt("type", danhMuc.getLoaiDanhMuc());
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_themdanhmuc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        tgbLoaiDanhMuc = view.findViewById(R.id.tgbLoaiDanhMuc);
        tgbLoaiDanhMuc.check(R.id.btnDanhMucThu);
        tgbLoaiDanhMuc.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch (checkedId){
                    case R.id.btnDanhMucThu:
                        buttonDanhMucThu();
                        break;
                    case R.id.btnDanhMucChi:
                        buttonDanhMucChi();
                        break;
                }
            }
        });

        txtTenDanhMuc = view.findViewById(R.id.txtTenViTien);
        btnDanhMucThu = view.findViewById(R.id.btnDanhMucThu);
        imgChonHinh = view.findViewById(R.id.imgChonHinh);
        imgChonHinh.setOnClickListener(this);
        expanHinhDanhMuc = view.findViewById(R.id.expanHinhDanhMuc);
        //Đổ dữ liệu ảnh vào RecyclerView
        rvHinhDanhMuc = view.findViewById(R.id.rvHinhDanhMuc);
        rvHinhDanhMuc.setLayoutManager(new GridLayoutManager(getContext(), 5));
        listHinh = Arrays.asList(getResources().getStringArray(R.array.img_cat));
        hinhDanhMucAdapter = new HinhDanhMucAdapter(getContext(), listHinh);
        rvHinhDanhMuc.setAdapter(hinhDanhMucAdapter);

        btnDanhMucChi = view.findViewById(R.id.btnDanhMucChi);
        btnHuyBo = view.findViewById(R.id.btnHuyBo);
        btnHuyBo.setOnClickListener(this);
        btnHoanThanh = view.findViewById(R.id.btnHoanThanh);
        btnHoanThanh.setOnClickListener(this);
        //Hiển thị thông tin khi cập nhật
        capNhatDanhMuc();
    }

    private void capNhatDanhMuc() {
        if (getArguments() != null) {
            idDanhMuc = getArguments().getInt("id");
            hinhDanhMuc = getArguments().getString("img");
            imgChonHinh.setImageResource(getIdHinh(getArguments().getString("img")));
            txtTenDanhMuc.setText(getArguments().getString("name"));
            loaiDanhMuc = getArguments().getInt("type");
            if (loaiDanhMuc == 1){
                buttonDanhMucThu();
            } else {
                buttonDanhMucChi();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgChonHinh:
                xuLyChonHinh();
                break;
            case R.id.btnHuyBo:
                Toast.makeText(getContext(), "Hủy bỏ", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.btnHoanThanh:
                xuLyThemDanhMuc();
                break;
        }
    }

    private void xuLyThemDanhMuc() {
        tenDanhMuc = txtTenDanhMuc.getText().toString().trim();
        if (TextUtils.isEmpty(hinhDanhMuc)){
            hinhDanhMuc = "cat_clipboard";
        }
        if (TextUtils.isEmpty(tenDanhMuc)){
            Toasty.error(getContext(),"Bạn chưa điền tên danh mục!",Toasty.LENGTH_SHORT,true).show();
            return;
        }
        if (loaiDanhMuc != 2){
            loaiDanhMuc = 1;
        }
        if (getArguments() != null) {
            DanhMuc danhMuc = new DanhMuc(idDanhMuc,tenDanhMuc,hinhDanhMuc,loaiDanhMuc);
            appViewModel.capNhatDanhMuc(danhMuc);
            Toasty.success(getContext(),"Cập nhật danh mục thành công",Toasty.LENGTH_SHORT,true).show();
        } else {
            DanhMuc danhMuc = new DanhMuc(tenDanhMuc, hinhDanhMuc, loaiDanhMuc);
            appViewModel.themDanhMuc(danhMuc);
            Toasty.success(getContext(),"Thêm danh mục thành công",Toasty.LENGTH_SHORT,true).show();
        }
    }

    private void xuLyChonHinh() {
        expanHinhDanhMuc.toggle();
        hinhDanhMucAdapter.setOnItemClickListener(new HinhDanhMucAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name) {
                imgChonHinh.setImageResource(getIdHinh(name));
                hinhDanhMuc = name;
            }
        });
    }

    private int getIdHinh(String name) {
        int drawableResourceId = getContext().getResources().getIdentifier(name, "drawable", getContext().getPackageName());
        return drawableResourceId;
    }

    private void buttonDanhMucThu() {
        btnDanhMucThu.setBackgroundColor(getResources().getColor(R.color.button_checked));
        btnDanhMucThu.setTextColor(getResources().getColor(R.color.white));
        btnDanhMucChi.setBackgroundColor(getResources().getColor(R.color.button_uncheck));
        btnDanhMucChi.setTextColor(getResources().getColor(R.color.black));
        loaiDanhMuc = 1;
    }

    private void buttonDanhMucChi() {
        btnDanhMucChi.setBackgroundColor(getResources().getColor(R.color.button_checked));
        btnDanhMucChi.setTextColor(getResources().getColor(R.color.white));
        btnDanhMucThu.setBackgroundColor(getResources().getColor(R.color.button_uncheck));
        btnDanhMucThu.setTextColor(getResources().getColor(R.color.black));
        loaiDanhMuc = 2;
    }
}
