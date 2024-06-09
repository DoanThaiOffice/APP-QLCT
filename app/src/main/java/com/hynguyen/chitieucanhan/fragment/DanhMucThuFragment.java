package com.hynguyen.chitieucanhan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.adapter.DanhMucAdapter;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class DanhMucThuFragment extends Fragment {
    private View view;
    private RecyclerView rvDanhMucThu;
    private AppViewModel appViewModel;
    private DanhMucAdapter danhMucAdapter;
    private LiveData<List<DanhMuc>> listDanhMuc;
    private DanhMuc danhMucChon;
    private TextView txtTongSoDanhMuc;
    private int soDanhMucThu;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_danhmucthu, container, false);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        rvDanhMucThu = view.findViewById(R.id.rvDanhMucThu);
        rvDanhMucThu.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rvDanhMucThu.setHasFixedSize(true);
        danhMucAdapter = new DanhMucAdapter(getContext());
        rvDanhMucThu.setAdapter(danhMucAdapter);
        listDanhMuc = appViewModel.tatCaDanhMucThu();
        listDanhMuc.observe(getActivity(), new Observer<List<DanhMuc>>() {
            @Override
            public void onChanged(List<DanhMuc> danhMucList) {
                danhMucAdapter.submitList(danhMucList);
                soDanhMucThu = danhMucList.size();
                txtTongSoDanhMuc.setText("Tổng số: " + soDanhMucThu);
            }
        });
        danhMucAdapter.setOnItemClickListener(new DanhMucAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DanhMuc danhMuc) {
                showBottomSheetDanhMuc();
                danhMucChon = danhMuc;
            }
        });
        txtTongSoDanhMuc = view.findViewById(R.id.txtTongSoDanhMuc);
        return view;
    }

    //Hiển thị menu Xóa/Chỉnh sửa danh mục
    private void showBottomSheetDanhMuc() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottomsheet_menu_xoa_sua);
        bottomSheetDialog.show();
        Button btnXoa = bottomSheetDialog.findViewById(R.id.btnXoa);
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soDanhMucThu>1) {
                    if (danhMucChon != null) {
                        appViewModel.xoaDanhMuc(danhMucChon);
                        bottomSheetDialog.dismiss();
                    } else {
                        Toasty.error(getContext(), "Lỗi! Hãy thử lại", Toasty.LENGTH_SHORT, true).show();
                        bottomSheetDialog.dismiss();
                    }
                } else {
                    Toasty.error(getContext(), "Phải có ít nhất 1 danh mục!", Toasty.LENGTH_SHORT, true).show();
                }
            }
        });
        Button btnChinhSua = bottomSheetDialog.findViewById(R.id.btnChinhSua);
        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemDanhMucFragment addPhotoBottomDialogFragment = ThemDanhMucFragment.newInstance(danhMucChon);
                addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(), ThemDanhMucFragment.TAG);
                bottomSheetDialog.dismiss();
            }
        });
    }
}
