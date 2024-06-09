package com.hynguyen.chitieucanhan.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.mdel.LoaiTienTe;

import es.dmoral.toasty.Toasty;

public class SuaLoaiTienTeFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    public static final String TAG = "LoaiTienTe";
    private AppViewModel appViewModel;
    private EditText txtLoaiTienTe;
    private Button btnHuyBo, btnHoanThanh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sualoaitiente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        txtLoaiTienTe = view.findViewById(R.id.txtLoaiTienTe);
        if (appViewModel.loaiTienTe() != null) {
            txtLoaiTienTe.setHint(appViewModel.loaiTienTe().getName());
            txtLoaiTienTe.setText(appViewModel.loaiTienTe().getName());
        }
        btnHuyBo = view.findViewById(R.id.btnHuyBo);
        btnHuyBo.setOnClickListener(this);
        btnHoanThanh = view.findViewById(R.id.btnHoanThanh);
        btnHoanThanh.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHuyBo:
                dismiss();
                break;
            case R.id.btnHoanThanh:
                xuLySuaLoaiTienTe();
                break;
        }
    }

    private void xuLySuaLoaiTienTe() {
        String loaitiente = txtLoaiTienTe.getText().toString().trim();
        if (TextUtils.isEmpty(loaitiente)) {
            Toasty.error(getContext(), "Không được để trống", Toasty.LENGTH_SHORT, true).show();
            return;
        }
        if (appViewModel.loaiTienTe() != null) {
            if (!loaitiente.equals(appViewModel.loaiTienTe().getName())) {
                LoaiTienTe loaiTienTe = new LoaiTienTe(1, loaitiente);
                appViewModel.capNhatLoaiTienTe(loaiTienTe);
            }
        } else {
            LoaiTienTe loaiTienTe = new LoaiTienTe(loaitiente);
            appViewModel.themLoaiTienTe(loaiTienTe);
        }
        Toasty.success(getContext(), "Cập nhật thành công!", Toasty.LENGTH_SHORT, true).show();
        dismiss();
    }

    public static SuaLoaiTienTeFragment newInstance() {
        return new SuaLoaiTienTeFragment();
    }

}
