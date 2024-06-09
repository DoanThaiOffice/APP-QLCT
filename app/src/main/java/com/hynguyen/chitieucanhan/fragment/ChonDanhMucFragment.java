package com.hynguyen.chitieucanhan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.adapter.DanhMucAdapter;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;

import java.util.List;

public class ChonDanhMucFragment extends BottomSheetDialogFragment {
    private View view;
    public static final String TAG = "ChonDanhMuc";
    private AppViewModel appViewModel;
    private MaterialButtonToggleGroup tgbLoaiDanhMuc;
    private MaterialButton btnDanhMucThu, btnDanhMucChi;
    private DanhMucAdapter danhMucAdapter;
    private RecyclerView rvChonDanhMuc;
    private LiveData<List<DanhMuc>> listDanhMuc;
    private List<DanhMuc> danhMucs;
    private int loaiDanhMuc = 1;
    private ChonDanhMucFragmentListener listener;

    public interface ChonDanhMucFragmentListener {
        public void onClickListener(DanhMuc danhMuc);
    }

    public void setDialogFragmentListener(ChonDanhMucFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ChonDanhMucFragment newInstance() {
        return new ChonDanhMucFragment();
    }

    //Lấy thông tin danh mục cần cập nhật
    public static ChonDanhMucFragment newInstance(DanhMuc danhMuc) {
        ChonDanhMucFragment dialog = new ChonDanhMucFragment();
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
        view = inflater.inflate(R.layout.fragment_chondanhmuc, container, false);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        //Chọn loại danh mục
        tgbLoaiDanhMuc = view.findViewById(R.id.tgbLoaiDanhMuc);
        btnDanhMucThu = view.findViewById(R.id.btnDanhMucThu);
        btnDanhMucChi = view.findViewById(R.id.btnDanhMucChi);
        tgbLoaiDanhMuc.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch (checkedId) {
                    case R.id.btnDanhMucThu:
                        buttonDanhMucThu();
                        break;
                    case R.id.btnDanhMucChi:
                        buttonDanhMucChi();
                        break;
                }
            }
        });

        if (getArguments() != null && getArguments().getInt("type") ==1 ){
            buttonDanhMucThu();
        } else {
            buttonDanhMucChi();
        }

        //Đổ dữ liệu vào Recycler View
        rvChonDanhMuc = view.findViewById(R.id.rvChonDanhMuc);
        rvChonDanhMuc.setLayoutManager(new GridLayoutManager(getContext(), 4));
        danhMucAdapter = new DanhMucAdapter(getContext());
        rvChonDanhMuc.setAdapter(danhMucAdapter);
        danhMucAdapter.setOnItemClickListener(new DanhMucAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DanhMuc danhMuc) {
                listener.onClickListener(danhMuc);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void buttonDanhMucThu() {
        btnDanhMucThu.setBackgroundColor(getResources().getColor(R.color.button_checked));
        btnDanhMucThu.setTextColor(getResources().getColor(R.color.white));
        btnDanhMucChi.setBackgroundColor(getResources().getColor(R.color.button_uncheck));
        btnDanhMucChi.setTextColor(getResources().getColor(R.color.black));
        loaiDanhMuc = 1;
        getListDanhMuc(appViewModel.tatCaDanhMucThu());
    }

    private void buttonDanhMucChi() {
        btnDanhMucChi.setBackgroundColor(getResources().getColor(R.color.button_checked));
        btnDanhMucChi.setTextColor(getResources().getColor(R.color.white));
        btnDanhMucThu.setBackgroundColor(getResources().getColor(R.color.button_uncheck));
        btnDanhMucThu.setTextColor(getResources().getColor(R.color.black));
        loaiDanhMuc = 2;
        getListDanhMuc(appViewModel.tatCaDanhMucChi());
    }

    private void getListDanhMuc(LiveData<List<DanhMuc>> listDanhMuc) {
        listDanhMuc.observe(getActivity(), new Observer<List<DanhMuc>>() {
            @Override
            public void onChanged(List<DanhMuc> danhMucList) {
                danhMucAdapter.submitList(danhMucList);
                danhMucs = danhMucList;
            }
        });
    }
}
